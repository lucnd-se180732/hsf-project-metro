package com.hsf302.config;

import com.hsf302.enums.TicketType;
import com.hsf302.pojo.*;
import com.hsf302.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Configuration
public class SampleDataConfig implements CommandLineRunner {

    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private TrainRepository trainRepository;
    @Autowired
    private TrainStationScheduleRepository scheduleRepository;
    @Autowired
    private TicketConfigRepository ticketConfigRepository;

    @Override
    public void run(String... args) throws Exception {
        // 1. Tạo tuyến
        Route route1 = new Route();
        route1.setRouteCode("LINE1");
        route1.setName("Tuyến Metro Số 1");
        route1.setDescription("Tuyến đi từ Bến Thành đến Suối Tiên (và ngược lại)");
        route1.setColorHex("#FF0000");
        routeRepository.save(route1);

        // 2. Tạo danh sách 14 ga (3 ngầm + 11 trên cao)
        Station[] stations = new Station[]{
                createStation("M01", "Bến Thành", "Q1, TP.HCM", 0.0, 1, route1, 10.770809, 106.697543),
                createStation("M02", "Nhà Hát Thành Phố", "Q1, TP.HCM", 0.4, 2, route1, 10.775292, 106.701838),
                createStation("M03", "Ba Son", "Q1, TP.HCM", 1.0, 3, route1, 10.781537, 106.707998),
                createStation("M04", "Văn Thánh", "Q.Bình Thạnh", 2.5, 4, route1, 10.796010, 106.715503),
                createStation("M05", "Tân Cảng", "Q.Bình Thạnh", 4.0, 5, route1, 10.798549, 106.723251),
                createStation("M06", "Thảo Điền", "TP.Thủ Đức", 5.5, 6, route1, 10.800445, 106.733662),
                createStation("M07", "An Phú", "TP.Thủ Đức", 6.5, 7, route1, 10.802140, 106.742247),
                createStation("M08", "Rạch Chiếc", "TP.Thủ Đức", 8.0, 8, route1, 10.808526,106.755272),
                createStation("M09", "Phước Long", "TP.Thủ Đức", 10.0, 9, route1, 10.821403,106.758189),
                createStation("M10", "Bình Thái", "TP.Thủ Đức", 12.0, 10, route1, 10.832617,106.763896),
                createStation("M11", "Thủ Đức", "TP.Thủ Đức", 13.5, 11, route1, 10.846380,106.771642),
                createStation("M12", "Khu Công Nghệ Cao", "TP.Thủ Đức", 15.0, 12, route1, 10.858969,106.788835),
                createStation("M13", "Đại học Quốc Gia", "TP.Thủ Đức", 17.5, 13, route1, 10.866279,106.801200),
                createStation("M14", "Suối Tiên", "TP.Thủ Đức", 19.7, 14, route1, 10.879526,106.814087)
        };

            stationRepository.saveAll(Arrays.asList(stations));


        int numTrains = 9;
        LocalDateTime startTime = LocalDateTime.of(2025, 7, 1, 6, 30);

        for (int i = 0; i < numTrains; i++) {
            // Tạo tàu
            Train train = new Train();
            train.setTrainCode("T" + (i + 1));
            train.setDescription("Tàu số " + (i + 1));
            train.setRoute(route1);
            trainRepository.save(train);


            boolean reverse = i % 2 != 0;
            Station[] stationOrder = reverse ? reverseArray(stations) : stations;

            LocalDateTime baseTime = startTime.plusMinutes(i * 15);
            List<TrainStationSchedule> schedules = new ArrayList<>();

            for (int j = 0; j < stationOrder.length; j++) {
                TrainStationSchedule schedule = new TrainStationSchedule();
                schedule.setTrain(train);
                schedule.setStation(stationOrder[j]);
                schedule.setStationOrder(j + 1);
                schedule.setArrivalTime(baseTime.plusMinutes(j * 4));
                schedule.setDepartureTime(baseTime.plusMinutes(j * 4 + 2));
                schedule.setStart(j == 0);
                schedule.setEnd(j == stationOrder.length - 1);
                schedules.add(schedule);
            }

                scheduleRepository.saveAll(schedules);
            }

            // 4. Tạo ticket configs
            initTicketConfigs();
        }
    }
    private void initTicketConfigs() {
        TicketConfig[] configs = {
                createTicketConfig(
                        TicketType.SINGLE,
                        BigDecimal.ZERO,
                        "Vé lượt - từ ga {from} đến ga {to}",
                        "Chỉ sử dụng được 1 lần trong ngày "
                ),
                createTicketConfig(
                        TicketType.ONE_DAY,
                        BigDecimal.valueOf(25000),
                        "Vé 1 ngày - Không giới hạn số lượt đi",
                        "Hết hiệu lực vào cuối ngày"
                ),
                createTicketConfig(
                        TicketType.THREE_DAY,
                        BigDecimal.valueOf(70000),
                        "Vé 3 ngày - Không giới hạn số lượt đi",
                        "Có hiệu lực trong 3 ngày"
                ),
                createTicketConfig(
                        TicketType.MONTHLY,
                        BigDecimal.valueOf(300000),
                        "Vé tháng - Không giới hạn số lượt đi",
                        "Có hiệu lực trong 1 tháng kể từ ngày kích hoạt"
                ),
                createTicketConfig(
                        TicketType.STUDENT_MONTHLY,
                        BigDecimal.valueOf(150000),
                        "Vé tháng sinh viên (Giảm 50%)- Không giới hạn số lượt đi",
                        "Chỉ áp dụng với sinh viên, có hiệu lực trong 1 tháng kể từ ngày kích hoạt"
                )
        };

        ticketConfigRepository.saveAll(Arrays.asList(configs));
    }

    private TicketConfig createTicketConfig(TicketType type, BigDecimal price, String description, String note) {
        TicketConfig config = new TicketConfig();
        config.setTicketType(type);
        config.setPrice(price);
        config.setDescription(description);
        config.setNote(note);
        return config;
    }

    private Station createStation(String code, String name, String address, Double distanceKm, int order, Route route, Double lat, Double lng) {
        Station station = new Station();
        station.setStationCode(code);
        station.setName(name);
        station.setAddress(address);
        station.setDistanceFromStartKm(distanceKm);
        station.setStationOrder(order);
        station.setRoute(route);
        station.setLatitude(lat);
        station.setLongitude(lng);
        return station;
    }

    private Station[] reverseArray(Station[] arr) {
        Station[] reversed = new Station[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reversed[i] = arr[arr.length - 1 - i];
        }
        return reversed;
    }
}
