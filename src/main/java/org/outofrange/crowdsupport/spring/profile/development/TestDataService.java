package org.outofrange.crowdsupport.spring.profile.development;

import org.outofrange.crowdsupport.model.*;
import org.outofrange.crowdsupport.persistence.PermissionRepository;
import org.outofrange.crowdsupport.persistence.RoleRepository;
import org.outofrange.crowdsupport.service.*;
import org.outofrange.crowdsupport.util.PermissionStore;
import org.outofrange.crowdsupport.util.RoleStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Profile("dev")
public class TestDataService {
    private static final Logger log = LoggerFactory.getLogger(TestDataService.class);

    @Inject
    private StateService stateService;

    @Inject
    private CityService cityService;

    @Inject
    private UserService userService;

    @Inject
    private PlaceService placeService;

    @Inject
    private DonationRequestService donationRequestService;

    @Inject
    private CommentService commentService;

    @Inject
    private PermissionRepository permissionRepository;

    @Inject
    private RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        log.info("Creating test data");

        createUsers();
        createStatesCitiesVenues();
    }

    private void createUsers() {
        log.info("Create users");
        User admin = new User("admin", "admin");
        User normal = new User("user", "user");

        Permission[] permissions = new Permission[] {
                permissionRepository.save(new Permission(PermissionStore.PROCESS_PLACE_REQUESTS)),
                permissionRepository.save(new Permission(PermissionStore.MANAGE_USERS)),
                permissionRepository.save(new Permission(PermissionStore.MANAGE_ROLES)),
                permissionRepository.save(new Permission(PermissionStore.MANAGE_CITIES)),
                permissionRepository.save(new Permission(PermissionStore.MANAGE_PLACES)),
                permissionRepository.save(new Permission(PermissionStore.MANAGE_STATES)),
                permissionRepository.save(new Permission(PermissionStore.QUERY_USERS))
        };

        Role adminRole = roleRepository.save(new Role(RoleStore.ADMIN, permissions).setSystemRole(true));
        Role userRole = roleRepository.save(new Role(RoleStore.USER).setSystemRole(true));

        admin.setRoles(Arrays.asList(adminRole, userRole));
        normal.setRoles(Arrays.asList(userRole));

        admin = userService.save(admin);
        normal = userService.save(normal);
    }

    private void createStatesCitiesVenues() {
        final String placeHolder = "/r/image/placeholder.jpg";

        State uk = new State("United Kingdom");
        State austria = new State("Austria");
        State germany = new State("Germany");
        State hungary = new State("Hungary");
        State greece = new State("Greece");

        final List<State> states = Arrays.asList(uk, austria, germany, hungary, greece);
        states.forEach(s -> s.setImagePath(placeHolder));
        uk.setImagePath("http://images.nationalgeographic.com/wpf/media-live/photos/000/032/cache/united-kingdom-bus_3218_600x450.jpg");
        states.forEach(s -> stateService.save(s));

        City preston = new City(uk, "Preston");
        City vienna = new City(austria, "Vienna");
        City salzburg = new City(austria, "Salzburg");
        City hamburg = new City(germany, "Hamburg");
        City london = new City(uk, "London");
        City munich = new City(germany, "Munich");
        City budapest = new City(hungary, "Budapest");
        City kiel = new City(germany, "Kiel");
        City frankfurt = new City(germany, "Frankfurt");
        City spielfeld = new City(austria, "Spielfeld");

        final List<City> cities = Arrays.asList(preston, vienna, salzburg, hamburg, london, munich, budapest, kiel,
                frankfurt, spielfeld);
        cities.forEach(c -> c.setImagePath(placeHolder));
        vienna.setImagePath("https://www.wien.info/media/images/40367-graben-einkaufen-shopping-altstadt-einkaufsstrassen-3to2.jpeg/image_teaser");
        cities.forEach(c -> cityService.save(c));

        Place trainOfHope = new Place(vienna, "Westbahnhof", "Caritas");
        trainOfHope.setImagePath(placeHolder);
        Place trainOfHopeMainStation = new Place(vienna, "Hauptbahnhof", "Train of Hope");
        trainOfHopeMainStation.setImagePath("https://pbs.twimg.com/profile_images/639504080411430912/WWA0UT5g.jpg");
        trainOfHope.setActive(true);
        trainOfHopeMainStation.setActive(true);
        placeService.save(trainOfHope);
        placeService.save(trainOfHopeMainStation);

        createRequests(trainOfHope);
    }

    private void createRequests(Place place) {
        User user1 = userService.loadUserByUsername("admin");
        User user2 = userService.loadUserByUsername("user");

        DonationRequest water = new DonationRequest();
        DonationRequest toothbrush = new DonationRequest();

        water.setPlace(place);
        toothbrush.setPlace(place);

        water.setQuantity(30);
        water.setTitle("Water");
        water.setDescription("We need 30l water, as soon as possible!");


        Comment waterComment1 = new Comment();
        Comment waterComment2 = new Comment();
        waterComment1.setQuantity(6);
        waterComment1.setAuthor(user1);
        waterComment1.setText("I can bring a six tray of water bottles! ETA: +1h");
        waterComment2.setAuthor(user2);
        waterComment2.setText("I'll bring some bottles from my office.");

        waterComment1 = commentService.save(waterComment1);
        waterComment2 = commentService.save(waterComment2);
        water = donationRequestService.save(water);

        water.addComment(waterComment1);
        water.addComment(waterComment2);

        water = donationRequestService.save(water);

        toothbrush.setTitle("Toothbrushes!");
        toothbrush.setDescription("We need new toothbrushes, a lot of them, all the time!");
        donationRequestService.save(toothbrush);
        Comment toothbrushComment = new Comment();
        toothbrushComment.setDonationRequest(toothbrush);
        toothbrushComment.setAuthor(user2);
        toothbrushComment.setText("Got 2 spare toothbrushes at home, I'll bring them tomorrow.");
        toothbrushComment = commentService.save(toothbrushComment);
        toothbrush.addComment(toothbrushComment);

        donationRequestService.save(toothbrush);


        DonationRequest donationRequest = new DonationRequest();
        donationRequest.setTitle("Baby food");
        donationRequest.setDescription("We need baby food!");
        donationRequest.setQuantity(10);
        donationRequest.setPlace(place);
        donationRequestService.save(donationRequest);
    }
}
