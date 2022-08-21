package com.classified.seller.controller;

import com.classified.seller.commons.dto.request.TargetRequest;
import com.classified.seller.commons.dto.response.TargetResponse;
import com.classified.seller.commons.entity.Target;
import com.classified.seller.commons.mapper.TargetMapper;
import com.classified.seller.commons.service.TargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/targets")
public class TargetController {

    @Autowired
    private TargetService targetService;

    private TargetMapper targetMapper = TargetMapper.MAPPER;

    @PostMapping("/")
    public TargetResponse addTarget(@RequestBody TargetRequest targetRequest) {
        Target target = targetService.saveTarget(targetMapper.toEntity(targetRequest));
        return targetMapper.toReponse(target);
    }

    @GetMapping("/")
    public List<TargetResponse> getAllTargets() {
        return targetService.getAllTargets().stream().map(target -> targetMapper.toReponse(target)).collect(Collectors.toList());
    }

}
