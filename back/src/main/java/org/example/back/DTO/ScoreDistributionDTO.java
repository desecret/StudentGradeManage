package org.example.back.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.css.CSSStyleRule;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDistributionDTO {
    private String scoreRange;
    private int count;
    private String courseName;
    private String courseCode;
}
