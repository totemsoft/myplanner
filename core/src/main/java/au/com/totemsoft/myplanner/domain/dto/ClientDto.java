package au.com.totemsoft.myplanner.domain.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import au.com.totemsoft.myplanner.api.bean.ICountry;
import au.com.totemsoft.myplanner.api.bean.ITitleCode;
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

    private ITitleCode title;

    @NotNull @Size(min = 2, max = 64)
    private String firstname;

    @NotNull @Size(min = 2, max = 64)
    private String surname;

    private Date dateOfBirth;

    private ICountry dobCountry;

}
