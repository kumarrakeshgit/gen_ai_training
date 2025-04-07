package com.epam.training.gen.ai.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LightModel {
    private int id;
    private String type;
    private Boolean isOn;
}
