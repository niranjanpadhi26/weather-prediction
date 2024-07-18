package com.sapient.weatherprediction.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Builder
@Data
public class UiViewModel  extends RepresentationModel<UiViewModel> {
    private List<Prediction> predictionList;
}
