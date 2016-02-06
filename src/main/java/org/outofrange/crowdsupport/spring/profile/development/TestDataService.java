package org.outofrange.crowdsupport.spring.profile.development;

import org.outofrange.crowdsupport.model.*;
import org.outofrange.crowdsupport.persistence.*;
import org.outofrange.crowdsupport.service.CityService;
import org.outofrange.crowdsupport.util.RoleStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;

@Service
@Profile("dev")
public class TestDataService {
    private static final Logger log = LoggerFactory.getLogger(TestDataService.class);

    @Inject
    private CityService cityService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PlaceRepository placeRepository;

    @Inject
    private DonationRequestRepository donationRequestRepository;

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

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

        Role adminRole = roleRepository.findOneByName(RoleStore.ADMIN).get();
        Role userRole = roleRepository.findOneByName(RoleStore.USER).get();

        admin.setRoles(Arrays.asList(adminRole, userRole));
        normal.setRoles(Collections.singletonList(userRole));

        admin.setPasswordHash(passwordEncoder.encode(admin.getPassword()));
        normal.setPasswordHash(passwordEncoder.encode(normal.getPassword()));

        userRepository.save(admin);
        userRepository.save(normal);
    }

    private void createStatesCitiesVenues() {
        final String placeHolder = "/r/image/placeholder.jpg";

        final City preston = cityService.load("preston", "uk").get();

        Place uclan = new Place(preston, "University of Central Lancashire", "uclan", "Foster Building");
        uclan.setImagePath(placeHolder);
        uclan.setActive(true);
        uclan = placeRepository.save(uclan);

        //createRequests(uclan);
    }

    /*private void createRequests(Place place) {
        User user1 = userRepository.findOneByUsername("admin").get();
        User user2 = userRepository.findOneByUsername("user").get();

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

        water = donationRequestRepository.save(water);
        waterComment1.setDonationRequest(water);
        waterComment2.setDonationRequest(water);
        commentRepository.save(waterComment1);
        commentRepository.save(waterComment2);


        toothbrush.setTitle("Toothbrushes!");
        toothbrush.setDescription("We need new toothbrushes, a lot of them, all the time!");
        donationRequestRepository.save(toothbrush);
        Comment toothbrushComment = new Comment();
        toothbrushComment.setDonationRequest(toothbrush);
        toothbrushComment.setAuthor(user2);
        toothbrushComment.setText("Got 2 spare toothbrushes at home, I'll bring them tomorrow.");
        toothbrushComment = commentRepository.save(toothbrushComment);
        toothbrush.addComment(toothbrushComment);

        donationRequestRepository.save(toothbrush);


        DonationRequest donationRequest = new DonationRequest();
        donationRequest.setTitle("Baby food");
        donationRequest.setDescription("We need baby food!");
        donationRequest.setQuantity(10);
        donationRequest.setPlace(place);
        donationRequestRepository.save(donationRequest);
    }*/
}
