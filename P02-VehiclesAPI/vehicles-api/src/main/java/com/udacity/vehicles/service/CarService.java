package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

  private final CarRepository repository;
  private final MapsClient mapsClient;
  private final PriceClient priceClient;

  @Autowired
  public CarService(
      CarRepository repository,
      @Qualifier("maps") WebClient mapsWebClient,
      @Qualifier("pricing") WebClient priceWebClient,
      ModelMapper modelMapper
  ) {
    this.repository = repository;
    this.mapsClient = new MapsClient(mapsWebClient, modelMapper);
    this.priceClient = new PriceClient(priceWebClient);
  }

  /**
   * Gathers a list of all vehicles
   *
   * @return a list of all vehicles in the CarRepository
   */
  public List<Car> list() {
    return repository.findAll().stream()
        .peek(this::fetchPriceAndAddress)
        .collect(Collectors.toList());
  }

  /**
   * Gets car information by ID (or throws exception if non-existent)
   *
   * @param id the ID number of the car to gather information on
   * @return the requested car's information, including location and price
   */
  public Car findById(Long id) {
    Car car = repository.findById(id).orElseThrow(CarNotFoundException::new);
    fetchPriceAndAddress(car);

    return car;
  }

  /**
   * Either creates or updates a vehicle, based on prior existence of car
   *
   * @param car A car object, which can be either new or existing
   * @return the new/updated car is stored in the repository
   */
  public Car save(Car car) {
    if (car.getId() != null) {
      return repository.findById(car.getId())
          .map(carToBeUpdated -> {
            carToBeUpdated.setDetails(car.getDetails());
            carToBeUpdated.setLocation(car.getLocation());
            carToBeUpdated = repository.save(carToBeUpdated);
            fetchPriceAndAddress(carToBeUpdated);
            return carToBeUpdated;
          }).orElseThrow(CarNotFoundException::new);
    }

    repository.save(car);
    fetchPriceAndAddress(car);

    return car;
  }

  /**
   * Deletes a given car by ID
   *
   * @param id the ID number of the car to delete
   */
  public void delete(Long id) {
    Car car = repository.findById(id).orElseThrow(CarNotFoundException::new);

    repository.delete(car);
    repository.save(car);
  }

  /**
   * Fetches and sets Car object in-place price and address by calling other separate services
   *
   * @param car @Car object
   */
  public void fetchPriceAndAddress(Car car) {
    String price = priceClient.getPrice(car.getId());
    car.setPrice(price);

    Location address = mapsClient.getAddress(car.getLocation());
    car.setLocation(address);
  }
}
