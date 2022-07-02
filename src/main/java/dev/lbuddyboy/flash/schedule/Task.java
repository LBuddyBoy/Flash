package dev.lbuddyboy.flash.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class Task {

    private String id;
    private Date date;
    private String command;

}
