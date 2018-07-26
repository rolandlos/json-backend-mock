package models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseData {

    private final String id;
    private final int version = 0;
    private final String apiId;
    private final String domain;
    private final String category;
    private final String nameD;
    private final String nameF;
    private final String nameI;
    private final String nameE;

}
