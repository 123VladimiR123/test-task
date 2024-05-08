package org.test.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.test.task.transferandentity.DTOList;
import org.test.task.transferandentity.DTOTicket;
import org.test.task.transferandentity.EntityTicket;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.countAll();
    }

    private void countAll() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");

        //Pre downloaded file
        File file = new File("tickets.json");
        List<DTOTicket> tickets = mapper.readValue(file, DTOList.class).getTickets();

        tickets.stream().map(e -> EntityTicket.builder()
                .arrival(LocalDateTime.parse(String.format("%5s", e.getArrival_time()).replace(" ", "0") + " " + e.getArrival_date(), formatter))
                .departure(LocalDateTime.parse(String.format("%5s", e.getDeparture_time()).replace(" ", "0") + " " + e.getDeparture_date(), formatter))
                .carrier(e.getCarrier())
                .build())
                .collect(Collectors.groupingBy(EntityTicket::getCarrier))
                .values()
                .forEach(e -> System.out.println("Перевозчик: " + e.get(0).getCarrier()
                        + " время перелёта: "
                        + e.stream()
                        .min(Comparator.comparing(f -> Duration.between(f.getDeparture(), f.getArrival())))
                        .map(f -> Duration.between(f.getDeparture(), f.getArrival()))
                        .get()
                        .toString()
                        .substring(2)
                        .replace("H", "ч ")
                        .replace("M", "м ")));

        tickets.sort(Comparator.comparing(DTOTicket::getPrice));
        System.out.print("Медианная цена: " + tickets.get(tickets.size()/2).getPrice());
        System.out.print(", средняя: " + tickets.stream().mapToInt(DTOTicket::getPrice).average().getAsDouble());
    }
}