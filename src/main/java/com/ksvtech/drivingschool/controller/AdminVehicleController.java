package com.ksvtech.drivingschool.controller;

import com.ksvtech.drivingschool.dto.VehicleRequest;
import com.ksvtech.drivingschool.dto.VehicleResponse;
import com.ksvtech.drivingschool.entity.VehicleType;
import com.ksvtech.drivingschool.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/vehicles")
@RequiredArgsConstructor
public class AdminVehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public String list(Model model) {
        List<VehicleResponse> vehicles = vehicleService.getAll();
        model.addAttribute("vehicles", vehicles);
        return "admin/vehicles/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("vehicle", new VehicleRequest());
        model.addAttribute("vehicleId", null);
        model.addAttribute("vehicleTypes", VehicleType.values());
        return "admin/vehicles/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("vehicle") VehicleRequest form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("vehicleId", null);
            model.addAttribute("vehicleTypes", VehicleType.values());
            return "admin/vehicles/form";
        }
        vehicleService.create(form);
        return "redirect:/admin/vehicles";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        VehicleResponse resp = vehicleService.get(id);
        VehicleRequest form = new VehicleRequest();
        form.setRegistrationNumber(resp.getRegistrationNumber());
        form.setType(resp.getType());
        form.setMake(resp.getMake());
        form.setModel(resp.getModel());
        form.setYearOfManufacture(resp.getYearOfManufacture());
        form.setFuelType(resp.getFuelType());
        form.setInsuranceExpiryDate(resp.getInsuranceExpiryDate());
        form.setFitnessExpiryDate(resp.getFitnessExpiryDate());
        form.setActive(resp.isActive());

        model.addAttribute("vehicle", form);
        model.addAttribute("vehicleId", id);
        model.addAttribute("vehicleTypes", VehicleType.values());
        return "admin/vehicles/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("vehicle") VehicleRequest form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("vehicleId", id);
            model.addAttribute("vehicleTypes", VehicleType.values());
            return "admin/vehicles/form";
        }
        vehicleService.update(id, form);
        return "redirect:/admin/vehicles";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return "redirect:/admin/vehicles";
    }
}
