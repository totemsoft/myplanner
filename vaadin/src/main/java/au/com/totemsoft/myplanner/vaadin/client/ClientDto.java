package au.com.totemsoft.myplanner.vaadin.client;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ClientDto {

    private final Long id;

    private final String name;

    private final Date dateOfBirth;

    private final String country;

}
