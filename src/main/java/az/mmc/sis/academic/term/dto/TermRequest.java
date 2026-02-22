package az.mmc.sis.academic.term.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TermRequest {
    @NotBlank
    private String code;

    @NotBlank
    private String name;
}
