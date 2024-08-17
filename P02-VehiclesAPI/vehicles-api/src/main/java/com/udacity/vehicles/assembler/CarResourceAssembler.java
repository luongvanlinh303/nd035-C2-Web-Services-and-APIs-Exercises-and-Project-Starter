package com.udacity.vehicles.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.udacity.vehicles.api.CarController;
import com.udacity.vehicles.domain.car.Car;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Maps the CarController to the Car class using HATEOAS
 */
@Component
public class CarResourceAssembler implements RepresentationModelAssembler<Car, EntityModel<Car>> {

  @Override
  public CollectionModel<EntityModel<Car>> toCollectionModel(Iterable<? extends Car> entities) {
    return RepresentationModelAssembler.super.toCollectionModel(entities)
        .add(linkTo(methodOn(CarController.class).list()).withSelfRel());
  }

  @Override
  public EntityModel<Car> toModel(Car car) {
    return EntityModel.of(car,
        linkTo(methodOn(CarController.class).get(car.getId())).withSelfRel(),
        linkTo(methodOn(CarController.class).list()).withRel("cars"));
  }
}
