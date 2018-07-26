package models;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class Self {

    private final String id;
    private final String openId;
    private final int version = 0;
    private final List<String> roles = Arrays.asList("user");

}
