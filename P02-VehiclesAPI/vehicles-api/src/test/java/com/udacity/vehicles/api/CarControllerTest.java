package com.udacity.vehicles.api;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.service.CarService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Implements testing of the CarController class.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CarControllerTest {
  private final Long ID = 10L;

  @Autowired
  private MockMvc mvc;

  @Autowired
  private JacksonTester<Car> json;

  @MockBean
  private CarService carService;

  @MockBean
  private PriceClient priceClient;

  @MockBean
  private MapsClient mapsClient;

  /**
   * Creates pre-requisites for testing, such as an example car.
   */
  @Before
  public void setup() {
    Car car = getCar();
    car.setId(ID);
    car.setPrice(getPrice());
    car.setLocation(getLocation());
    given(carService.save(any())).willReturn(car);
    given(carService.findById(any())).willReturn(car);
    given(carService.list()).willReturn(Collections.singletonList(car));
    given(priceClient.getPrice(anyLong())).willReturn(car.getPrice());
    given(mapsClient.getAddress(any())).willReturn(car.getLocation());
  }

  /**
   * Tests for successful creation of new car in the system
   *
   * @throws Exception when car creation fails in the system
   */
  @Test
  public void createCar() throws Exception {
    Car car = getCar(ID);
    mvc.perform(
            post(new URI("/cars"))
                .content(json.write(car).getJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
  }

  /**
   * Tests if the read operation appropriately returns a list of vehicles.
   *
   * @throws Exception if the read operation of the vehicle list fails
   */
  @Test
  public void listCars() throws Exception {
    Car car = getCar(ID);
    mvc.perform(
            get(new URI("/cars")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$['_embedded']['carList'][0]").hasJsonPath())
        .andExpect(jsonPath("$['_embedded']['carList'][0].id", is(car.getId().intValue())))
        .andExpect(jsonPath("$['_embedded']['carList'][0].condition", is(car.getCondition().toString())))
        .andExpect(jsonPath("$['_embedded']['carList'][0].details").hasJsonPath())
        .andExpect(jsonPath("$['_embedded']['carList'][0].['details'].body", is(car.getDetails().getBody())))
        .andExpect(jsonPath("$['_embedded']['carList'][0].['details'].model", is(car.getDetails().getModel())))
        .andExpect(jsonPath("$['_embedded']['carList'][0].['details'].manufacturer").hasJsonPath())
        .andExpect(jsonPath("$['_embedded']['carList'][0].['details'].['manufacturer'].code", is(car.getDetails().getManufacturer().getCode())))
        .andExpect(jsonPath("$['_embedded']['carList'][0].['details'].['manufacturer'].name", is(car.getDetails().getManufacturer().getName())))
        .andExpect(jsonPath("$['_embedded']['carList'][0].['details'].numberOfDoors", is(car.getDetails().getNumberOfDoors())))
        .andExpect(jsonPath("$['_embedded']['carList'][0].['details'].fuelType", is(car.getDetails().getFuelType())))
        .andExpect(jsonPath("$['_embedded']['carList'][0].['details'].engine", is(car.getDetails().getEngine())))
        .andExpect(jsonPath("$['_embedded']['carList'][0].['details'].mileage", is(car.getDetails().getMileage())))
        .andExpect(jsonPath("$['_embedded']['carList'][0].['details'].modelYear", is(car.getDetails().getModelYear())))
        .andExpect(jsonPath("$['_embedded']['carList'][0].['details'].productionYear", is(car.getDetails().getProductionYear())))
        .andExpect(jsonPath("$['_embedded']['carList'][0].['details'].externalColor", is(car.getDetails().getExternalColor())))
        .andExpect(jsonPath("$['_embedded']['carList'][0].location").hasJsonPath())
        .andExpect(jsonPath("$['_embedded']['carList'][0].['location'].lat", is(car.getLocation().getLat())))
        .andExpect(jsonPath("$['_embedded']['carList'][0].['location'].lon", is(car.getLocation().getLon())))
        .andExpect(jsonPath("$['_embedded']['carList'][0].['location'].address", is(car.getLocation().getAddress())))
        .andExpect(jsonPath("$['_embedded']['carList'][0].['location'].city", is(car.getLocation().getCity())))
        .andExpect(jsonPath("$['_embedded']['carList'][0].['location'].state", is(car.getLocation().getState())))
        .andExpect(jsonPath("$['_embedded']['carList'][0].['location'].zip", is(car.getLocation().getZip())))
        .andExpect(jsonPath("$['_embedded']['carList'][0].price", is(car.getPrice())));

    verify(carService, times(1)).list();
  }

  /**
   * Tests the read operation for a single car by ID.
   *
   * @throws Exception if the read operation for a single car fails
   */
  @Test
  public void findCar() throws Exception {
    Car car = getCar(ID);
    mvc.perform(get("/cars/{id}", car.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").hasJsonPath())
        .andExpect(jsonPath("$.id", is(car.getId().intValue())))
        .andExpect(jsonPath("$.condition", is(car.getCondition().toString())))
        .andExpect(jsonPath("$.details").hasJsonPath())
        .andExpect(jsonPath("$.['details'].body", is(car.getDetails().getBody())))
        .andExpect(jsonPath("$.['details'].model", is(car.getDetails().getModel())))
        .andExpect(jsonPath("$.['details'].manufacturer").hasJsonPath())
        .andExpect(jsonPath("$.['details'].['manufacturer'].code", is(car.getDetails().getManufacturer().getCode())))
        .andExpect(jsonPath("$.['details'].['manufacturer'].name", is(car.getDetails().getManufacturer().getName())))
        .andExpect(jsonPath("$.['details'].numberOfDoors", is(car.getDetails().getNumberOfDoors())))
        .andExpect(jsonPath("$.['details'].fuelType", is(car.getDetails().getFuelType())))
        .andExpect(jsonPath("$.['details'].engine", is(car.getDetails().getEngine())))
        .andExpect(jsonPath("$.['details'].mileage", is(car.getDetails().getMileage())))
        .andExpect(jsonPath("$.['details'].modelYear", is(car.getDetails().getModelYear())))
        .andExpect(jsonPath("$.['details'].productionYear", is(car.getDetails().getProductionYear())))
        .andExpect(jsonPath("$.['details'].externalColor", is(car.getDetails().getExternalColor())))
        .andExpect(jsonPath("$.location").hasJsonPath())
        .andExpect(jsonPath("$.['location'].lat", is(car.getLocation().getLat())))
        .andExpect(jsonPath("$.['location'].lon", is(car.getLocation().getLon())))
        .andExpect(jsonPath("$.['location'].address", is(car.getLocation().getAddress())))
        .andExpect(jsonPath("$.['location'].city", is(car.getLocation().getCity())))
        .andExpect(jsonPath("$.['location'].state", is(car.getLocation().getState())))
        .andExpect(jsonPath("$.['location'].zip", is(car.getLocation().getZip())))
        .andExpect(jsonPath("$.price", is(car.getPrice())));

    verify(carService, times(1)).findById(car.getId());
  }

  /**
   * Tests the updating of a single car by ID.
   *
   * @throws Exception if the update operation of a vehicle fails
   */
  @Test
  public void updateCar() throws Exception {
    Car car = getCar(ID);
    car.setCondition(Condition.NEW);
    Details details = car.getDetails();
    details.setFuelType("Solar");
    details.setBody("Coupe");
    details.setModel("2002");
    car.setDetails(details);
    car.getDetails().setManufacturer(new Manufacturer(101, "Chevrolet"));
    mvc.perform(put("/cars/{id}", car.getId())
            .content(json.write(car).getJson())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").hasJsonPath())
        .andExpect(jsonPath("$.id", is(car.getId().intValue())))
        .andExpect(jsonPath("$.condition", is(car.getCondition().toString())))
        .andExpect(jsonPath("$.details").hasJsonPath())
        .andExpect(jsonPath("$.['details'].body", is(car.getDetails().getBody())))
        .andExpect(jsonPath("$.['details'].model", is(car.getDetails().getModel())))
        .andExpect(jsonPath("$.['details'].manufacturer").hasJsonPath())
        .andExpect(jsonPath("$.['details'].['manufacturer'].code", is(car.getDetails().getManufacturer().getCode())))
        .andExpect(jsonPath("$.['details'].['manufacturer'].name", is(car.getDetails().getManufacturer().getName())))
        .andExpect(jsonPath("$.['details'].numberOfDoors", is(car.getDetails().getNumberOfDoors())))
        .andExpect(jsonPath("$.['details'].fuelType", is(car.getDetails().getFuelType())))
        .andExpect(jsonPath("$.['details'].engine", is(car.getDetails().getEngine())))
        .andExpect(jsonPath("$.['details'].mileage", is(car.getDetails().getMileage())))
        .andExpect(jsonPath("$.['details'].modelYear", is(car.getDetails().getModelYear())))
        .andExpect(jsonPath("$.['details'].productionYear", is(car.getDetails().getProductionYear())))
        .andExpect(jsonPath("$.['details'].externalColor", is(car.getDetails().getExternalColor())))
        .andExpect(jsonPath("$.location").hasJsonPath())
        .andExpect(jsonPath("$.['location'].lat", is(car.getLocation().getLat())))
        .andExpect(jsonPath("$.['location'].lon", is(car.getLocation().getLon())))
        .andExpect(jsonPath("$.['location'].address", is(car.getLocation().getAddress())))
        .andExpect(jsonPath("$.['location'].city", is(car.getLocation().getCity())))
        .andExpect(jsonPath("$.['location'].state", is(car.getLocation().getState())))
        .andExpect(jsonPath("$.['location'].zip", is(car.getLocation().getZip())))
        .andExpect(jsonPath("$.price", is(car.getPrice())));

    verify(carService, times(1)).save(any());
  }

  /**
   * Tests the deletion of a single car by ID.
   *
   * @throws Exception if the delete operation of a vehicle fails
   */
  @Test
  public void deleteCar() throws Exception {
    Car car = getCar(ID);
    mvc.perform(delete("/cars/{id}", car.getId()))
        .andExpect(status().is2xxSuccessful());

    verify(carService, times(1)).delete(car.getId());
  }

  /**
   * Creates an example Car object for use in testing.
   *
   * @return an example Car object
   */
  private Car getCar() {
    Car car = new Car();
    car.setLocation(new Location(40.730610, -73.935242));
    Details details = new Details();
    Manufacturer manufacturer = new Manufacturer(101, "Chevrolet");
    details.setManufacturer(manufacturer);
    details.setModel("Impala");
    details.setMileage(32280);
    details.setExternalColor("white");
    details.setBody("sedan");
    details.setEngine("3.6L V6");
    details.setFuelType("Gasoline");
    details.setModelYear(2018);
    details.setProductionYear(2018);
    details.setNumberOfDoors(4);
    car.setDetails(details);
    car.setCondition(Condition.USED);
    return car;
  }

  /**
   * Creates a full example of Car object for use in testing
   *
   * @param vehicleId id of vehicle: to be set
   * @return full example of Car object including Address and Price
   */
  public Car getCar(Long vehicleId) {
    Car car = getCar();
    car.setId(vehicleId);
    car.setPrice(priceClient.getPrice(vehicleId));
    car.setLocation(mapsClient.getAddress(car.getLocation()));
    return car;
  }

  /**
   * Creates an example of price returned by pricing-service
   *
   * @return an example of price including currency
   */
  private String getPrice() {
    return String.format("%s USD", BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 5))
        .multiply(new BigDecimal("5000")).setScale(2, RoundingMode.HALF_UP));
  }

  /**
   * Creates an example of address returned by boogle-maps service
   *
   * @return an example of address
   */
  private Location getLocation() {
    Location location = new Location(33.28154075411885, -96.78057269846072);
    location.setAddress("3101 Knox St.");
    location.setZip("75205");
    location.setState("Dallas, TX");
    return location;
  }
}