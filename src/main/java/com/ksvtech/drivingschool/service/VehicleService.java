package com.ksvtech.drivingschool.service;

import com.ksvtech.drivingschool.dto.VehicleRequest;
import com.ksvtech.drivingschool.dto.VehicleResponse;
import com.ksvtech.drivingschool.entity.Vehicle;
import com.ksvtech.drivingschool.exception.NotFoundException;
import com.ksvtech.drivingschool.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleResponse create(VehicleRequest request) {
        Vehicle vehicle = Vehicle.builder()
                .registrationNumber(request.getRegistrationNumber())
                .type(request.getType())
                .make(request.getMake())
                .model(request.getModel())
                .yearOfManufacture(request.getYearOfManufacture())
                .fuelType(request.getFuelType())
                .insuranceExpiryDate(request.getInsuranceExpiryDate())
                .fitnessExpiryDate(request.getFitnessExpiryDate())
                .active(request.getActive() == null ? true : request.getActive())
                .notes(request.getNotes())
                .build();

        vehicle = vehicleRepository.save(vehicle);
        return toResponse(vehicle);
    }

    public VehicleResponse update(Long id, VehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vehicle not found"));

        vehicle.setRegistrationNumber(request.getRegistrationNumber());
        vehicle.setType(request.getType());
        vehicle.setMake(request.getMake());
        vehicle.setModel(request.getModel());
        vehicle.setYearOfManufacture(request.getYearOfManufacture());
        vehicle.setFuelType(request.getFuelType());
        vehicle.setInsuranceExpiryDate(request.getInsuranceExpiryDate());
        vehicle.setFitnessExpiryDate(request.getFitnessExpiryDate());
        vehicle.setActive(request.getActive() == null ? vehicle.isActive() : request.getActive());
        vehicle.setNotes(request.getNotes());

        vehicle = vehicleRepository.save(vehicle);
        return toResponse(vehicle);
    }

    public VehicleResponse get(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vehicle not found"));
        return toResponse(vehicle);
    }

    public List<VehicleResponse> getAll() {
        return vehicleRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public void delete(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new NotFoundException("Vehicle not found");
        }
        vehicleRepository.deleteById(id);
    }

    private VehicleResponse toResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .registrationNumber(vehicle.getRegistrationNumber())
                .type(vehicle.getType())
                .make(vehicle.getMake())
                .model(vehicle.getModel())
                .yearOfManufacture(vehicle.getYearOfManufacture())
                .fuelType(vehicle.getFuelType())
                .insuranceExpiryDate(vehicle.getInsuranceExpiryDate())
                .fitnessExpiryDate(vehicle.getFitnessExpiryDate())
                .active(vehicle.isActive())
                .build();
    }
}
