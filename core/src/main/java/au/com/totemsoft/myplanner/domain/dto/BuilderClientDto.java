package au.com.totemsoft.myplanner.domain.dto;

import au.com.totemsoft.myplanner.api.bean.IClient;
import au.com.totemsoft.myplanner.api.bean.IClientView;

public class BuilderClientDto {

    //@Builder(buildMethodName = "client")
    public static ClientDto client(IClient client) {
        return ClientDto.builder()
            .id(client.getId() == null ? null : client.getId().intValue())
            .title(client.getTitle())
            .firstname(client.getFirstname())
            .surname(client.getSurname())
            .dateOfBirth(client.getDateOfBirth())
            .dobCountry(client.getDobCountry())
            .build();
    }

    //@Builder(buildMethodName = "clientView")
    public static ClientDto clientView(IClientView clientView) {
        return ClientDto.builder()
            .id(clientView.getId() == null ? null : clientView.getId().intValue())
            .title(clientView.getTitle())
            .firstname(clientView.getFirstname())
            .surname(clientView.getSurname())
            .dateOfBirth(clientView.getDateOfBirth())
            .dobCountry(clientView.getDobCountry())
            .build();
    }

}
