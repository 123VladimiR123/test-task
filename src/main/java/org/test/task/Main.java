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

        /*
        Для простоты прописал литералами
         */
        main.countAll("tickets.json");
    }

    private void countAll(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");

        //Pre downloaded file
        File file = new File(path);
        List<DTOTicket> tickets = mapper.readValue(file, DTOList.class).getTickets()
                .stream()
                .filter(e -> e.getDestination_name().equals("Тель-Авив") && e.getOrigin_name().equals("Владивосток"))
                .collect(Collectors.toList());

        tickets.stream()
                .map(e -> EntityTicket.builder()
                .arrival(LocalDateTime.parse(String.format("%5s", e.getArrival_time())
                        .replace(" ", "0") + " " + e.getArrival_date(), formatter))
                .departure(LocalDateTime.parse(String.format("%5s", e.getDeparture_time())
                        .replace(" ", "0") + " " + e.getDeparture_date(), formatter))
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

        double med = ((tickets.size() % 2 == 1)
                ? tickets.get(tickets.size() / 2).getPrice()
                : (tickets.get(tickets.size() / 2).getPrice() + tickets.get(tickets.size() / 2 - 1).getPrice()) / 2.0);
        double aver = tickets.stream().mapToInt(DTOTicket::getPrice).average().getAsDouble();

        System.out.printf("Медианная цена: %.2f", med);
        System.out.printf(" средняя: %.2f", aver);
        System.out.printf("\nРазница %.2f", Math.abs(med - aver));
    }
}