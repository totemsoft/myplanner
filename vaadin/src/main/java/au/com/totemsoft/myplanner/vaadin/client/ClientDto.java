package au.com.totemsoft.myplanner.vaadin.client;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ClientDto {

    private Long id;

    @NotNull @Max(64)
    private String name;

    private Date dateOfBirth;

    private String country;

}
