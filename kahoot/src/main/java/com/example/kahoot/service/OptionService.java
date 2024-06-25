package com.example.kahoot.service;

import com.example.kahoot.entity.Option;
import com.example.kahoot.repository.OptionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionService {

    @Autowired
    private OptionRepository optionRepository;

    public Option saveOption(Option option) {
        return optionRepository.save(option);
    }

    public List<Option> getOptions() {
        return optionRepository.findAll();
    }

    public Option getOptionById(int id) {
        return optionRepository.findById(id).orElse(null);
    }

    public void deleteOption(int id) {
        optionRepository.deleteById(id);
    }
}