package com.hsf302.config;

import com.hsf302.enums.Role;
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

    @Autowired
    private BusRouteRepository busRouteRepository;

    @Autowired
    private StationBusRouteRepository stationBusRouteRepository;

    @Autowired
    private FareMatrixRepository fareRepo;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        if (!userRepository.existsByEmail(("imquocvu1@gmail.com"))) {
            User user = new User();
            user.setEmail("imquocvu1@gmail.com");
            user.setRole(Role.ADMIN);
            userRepository.save(user);
        }



        if (routeRepository.count() > 0) return;
        // 1. Tạo tuyến
        Route route1 = new Route();
        route1.setRouteCode("LINE1");
        route1.setName("Tuyến Metro Số 1");
        route1.setDescription("Tuyến đi từ Bến Thành đến Suối Tiên (và ngược lại)");
        route1.setColorHex("#FF0000");
        routeRepository.save(route1);

        // 2. Danh sách 14 ga
        Station[] stations = new Station[]{
                newStation("M01", "Bến Thành", "Q1, TP.HCM", 0.0, 1, route1, 10.770809, 106.697543),
                newStation("M02", "Nhà Hát Thành Phố", "Q1, TP.HCM", 0.4, 2, route1, 10.775292, 106.701838),
                newStation("M03", "Ba Son", "Q1, TP.HCM", 1.0, 3, route1, 10.781537, 106.707998),
                newStation("M04", "Văn Thánh", "Q.Bình Thạnh", 2.5, 4, route1, 10.796010, 106.715503),
                newStation("M05", "Tân Cảng", "Q.Bình Thạnh", 4.0, 5, route1, 10.798549, 106.723251),
                newStation("M06", "Thảo Điền", "TP.Thủ Đức", 5.5, 6, route1, 10.800445, 106.733662),
                newStation("M07", "An Phú", "TP.Thủ Đức", 6.5, 7, route1, 10.802140, 106.742247),
                newStation("M08", "Rạch Chiếc", "TP.Thủ Đức", 8.0, 8, route1, 10.808526, 106.755272),
                newStation("M09", "Phước Long", "TP.Thủ Đức", 10.0, 9, route1, 10.821403, 106.758189),
                newStation("M10", "Bình Thái", "TP.Thủ Đức", 12.0, 10, route1, 10.832617, 106.763896),
                newStation("M11", "Thủ Đức", "TP.Thủ Đức", 13.5, 11, route1, 10.846380, 106.771642),
                newStation("M12", "Khu Công Nghệ Cao", "TP.Thủ Đức", 15.0, 12, route1, 10.858969, 106.788835),
                newStation("M13", "Đại học Quốc Gia", "TP.Thủ Đức", 17.5, 13, route1, 10.866279, 106.801200),
                newStation("M14", "Suối Tiên", "TP.Thủ Đức", 19.7, 14, route1, 10.879526, 106.814087)
        };
        stationRepository.saveAll(Arrays.asList(stations));

        if (busRouteRepository.count() == 0) {
            Date now = new Date();
            List<BusRoute> busRoutes = List.of(
                    newBusRoute("153", "Bến tàu thủy Bình An – Đường Liên Phường", now),
                    newBusRoute("154", "Thạnh Mỹ Lợi – Masteri An Phú", now),
                    newBusRoute("155", "Bx. Sài Gòn – Nhà hát TP", now),
                    newBusRoute("156", "Bx. Sài Gòn – Ga Hòa Hưng", now),
                    newBusRoute("157", "Văn Thánh – Chung cư Đức Khải", now),
                    newBusRoute("158", "Văn Thánh – Thanh Đa", now),
                    newBusRoute("159", "Ngô Tất Tố – Hàng Xanh", now),
                    newBusRoute("160", "Văn Thánh – Vinhomes Central Park", now),
                    newBusRoute("161", "Văn Thánh – Bến xe Ngã Tư Ga", now),
                    newBusRoute("162", "Man Thiện – THCS Hoa Lư", now),
                    newBusRoute("163", "Cao đẳng Công Thương – Phước Bình", now),
                    newBusRoute("164", "ĐH Nông lâm – Chung cư Topaz", now),
                    newBusRoute("165", "ĐH Nông lâm – Khu CNC", now),
                    newBusRoute("166", "ĐH Quốc gia – Suối Tiên", now),
                    newBusRoute("167", "ĐH Nông lâm – Linh Trung 1", now),
                    newBusRoute("168", "ĐH SPKT – Ngã tư Bình Thái", now),
                    newBusRoute("169", "Vincom Thủ Đức – Ngã tư Tây Hòa", now)
            );
            busRouteRepository.saveAll(busRoutes);
        }

        List<StationBusRoute> stationBusRoutes = List.of(
                new StationBusRoute(getStation(6L), getBusRoute(1L), 250, 3),
                new StationBusRoute(getStation(7L), getBusRoute(1L), 10, 1),
                new StationBusRoute(getStation(8L), getBusRoute(2L), 20, 1),
                new StationBusRoute(getStation(7L), getBusRoute(2L), 100, 2),
                new StationBusRoute(getStation(1L), getBusRoute(3L), 30, 1),
                new StationBusRoute(getStation(2L), getBusRoute(3L), 40, 1),
                new StationBusRoute(getStation(1L), getBusRoute(4L), 30, 1),
                new StationBusRoute(getStation(2L), getBusRoute(4L), 50, 1),
                new StationBusRoute(getStation(5L), getBusRoute(5L), 60, 1),
                new StationBusRoute(getStation(6L), getBusRoute(5L), 50, 1),
                new StationBusRoute(getStation(5L), getBusRoute(6L), 20, 1),
                new StationBusRoute(getStation(4L), getBusRoute(7L), 150, 2),
                new StationBusRoute(getStation(4L), getBusRoute(8L), 150, 2),
                new StationBusRoute(getStation(5L), getBusRoute(9L), 20, 1),
                new StationBusRoute(getStation(10L), getBusRoute(10L), 40, 1),
                new StationBusRoute(getStation(11L), getBusRoute(10L), 30, 1),
                new StationBusRoute(getStation(10L), getBusRoute(11L), 40, 1),
                new StationBusRoute(getStation(13L), getBusRoute(12L), 15, 1),
                new StationBusRoute(getStation(14L), getBusRoute(12L), 300, 4),
                new StationBusRoute(getStation(12L), getBusRoute(13L), 10, 1),
                new StationBusRoute(getStation(14L), getBusRoute(14L), 350, 5),
                new StationBusRoute(getStation(12L), getBusRoute(15L), 10, 1),
                new StationBusRoute(getStation(11L), getBusRoute(16L), 60, 1),
                new StationBusRoute(getStation(10L), getBusRoute(17L), 40, 1),
                new StationBusRoute(getStation(3L), getBusRoute(3L), 350, 6),
                new StationBusRoute(getStation(5L), getBusRoute(8L), 20, 1),
                new StationBusRoute(getStation(13L), getBusRoute(14L), 15, 1),
                new StationBusRoute(getStation(10L), getBusRoute(16L), 40, 1)
        );
        stationBusRouteRepository.saveAll(stationBusRoutes);







        // 3. Tạo tàu và lịch trình
        int numTrains = 9;
        LocalDateTime startTime = LocalDateTime.of(2025, 7, 1, 6, 30);
        for (int i = 0; i < numTrains; i++) {
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

        // 4. Ticket configs
        TicketConfig[] configs = {
                newTicketConfig(TicketType.SINGLE, BigDecimal.ZERO, "Vé lượt - từ ga {from} đến ga {to}", "Chỉ sử dụng được 1 lần trong ngày "),
                newTicketConfig(TicketType.ONE_DAY, BigDecimal.valueOf(25000), "Vé 1 ngày - Không giới hạn số lượt đi", "Hết hiệu lực vào cuối ngày"),
                newTicketConfig(TicketType.THREE_DAY, BigDecimal.valueOf(70000), "Vé 3 ngày - Không giới hạn số lượt đi", "Có hiệu lực trong 3 ngày"),
                newTicketConfig(TicketType.MONTHLY, BigDecimal.valueOf(300000), "Vé tháng - Không giới hạn số lượt đi", "Có hiệu lực trong 1 tháng kể từ ngày kích hoạt"),
                newTicketConfig(TicketType.STUDENT_MONTHLY, BigDecimal.valueOf(150000), "Vé tháng sinh viên (Giảm 50%)", "Chỉ áp dụng với sinh viên, hiệu lực 1 tháng kể từ ngày kích hoạt")
        };
        ticketConfigRepository.saveAll(Arrays.asList(configs));



        if (fareRepo.count() == 0) {
            String[] stationsExac = {
                    "Bến Thành", "Nhà Hát Thành Phố", "Ba Son", "Văn Thánh",
                    "Tân Cảng", "Thảo Điền", "An Phú", "Rạch Chiếc",
                    "Phước Long", "Bình Thái", "Thủ Đức", "Khu Công Nghệ Cao",
                    "Suối Tiên", "Bến Xe Miền Đông mới"
            };

            int baseFare = 8000;
            int fareStep = 1000;

            for (int i = 0; i < stationsExac.length; i++) {
                for (int j = 0; j < stationsExac.length; j++) {
                    if (i != j) {
                        int distance = Math.abs(i - j);
                        int fare = baseFare + distance * fareStep;
                        fareRepo.save(new FareMatrix(stationsExac[i], stationsExac[j], fare));
                    }
                }
            }
        }
    }

    // Helper tạo station
    private Station newStation(String code, String name, String address, Double distanceKm, int order, Route route, Double lat, Double lng) {
        Station s = new Station();
        s.setStationCode(code);
        s.setName(name);
        s.setAddress(address);
        s.setDistanceFromStartKm(distanceKm);
        s.setStationOrder(order);
        s.setRoute(route);
        s.setLatitude(lat);
        s.setLongitude(lng);
        return s;
    }

    // Helper tạo ticket config
    private TicketConfig newTicketConfig(TicketType type, BigDecimal price, String description, String note) {
        TicketConfig config = new TicketConfig();
        config.setTicketType(type);
        config.setPrice(price);
        config.setDescription(description);
        config.setNote(note);
        return config;
    }

    // Helper đảo mảng station
    private Station[] reverseArray(Station[] arr) {
        Station[] reversed = new Station[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reversed[i] = arr[arr.length - 1 - i];
        }
        return reversed;
    }

    private BusRoute newBusRoute(String routeNumber, String name, Date now) {
        BusRoute route = new BusRoute();
        route.setRouteNumber(routeNumber);
        route.setName(name);
        route.setDescription("Tuyến " + routeNumber + ": " + name);
        route.setOperator("HSF Bus");
        route.setIsActive(true);
        route.setCreatedAt(now);
        route.setUpdatedAt(now);
        return route;
    }

    private Station getStation(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Station ID " + id + " not found"));
    }

    private BusRoute getBusRoute(Long id) {
        return busRouteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BusRoute ID " + id + " not found"));
    }

}
