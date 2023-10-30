package com.sincera.intern.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sincera.intern.dto.*;
import com.sincera.intern.model.Role;
import com.sincera.intern.model.User;
import com.sincera.intern.repository.RoleRepository;
import com.sincera.intern.repository.SiteRepository;
import com.sincera.intern.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Controller
public class TNIController {

    private static final Logger log = LoggerFactory.getLogger(TNIController.class);
    private static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";

    @Autowired
    SiteService siteService;
    @Autowired
    ShelfService shelfService;
    @Autowired
    SlotService slotService;
    @Autowired
    CardService cardService;
    @Autowired
    PortService portService;
    @Autowired
    MasterService masterService;
    @Autowired
    UserService userService;
    @Autowired
    DataProcessingService dataProcessingService;
    @Autowired
    SiteRepository siteRepository;

    @Autowired
    Environment env;
    @Autowired
    RoleRepository roleRepository;

    @Value("#{'${inventory.status.list}'.split(',')}")
    private List<String> statusList;

    @Value("#{'${inventory.site.type.list}'.split(',')}")
    private List<String> siteTypeList;

    @Value("#{'${inventory.shelf.type.list}'.split(',')}")
    private List<String> shelfTypeList;

    @Value("#{'${inventory.card.type.list}'.split(',')}")
    private List<String> cardTypeList;

    @Value("#{'${inventory.port.type.list}'.split(',')}")
    private List<String> portTypeList;
    @Value("#{'${inventory.port.bw.list}'.split(',')}")
    private List<String> portBandwidthList;

    @Value("#{'${inventory.entity.list}'.split(',')}")
    private List<String> entitylist;

    @Value("#{'${inventory.download.list}'.split(',')}")
    private List<String> downloadList;
//    @PostMapping(path = "/site/create")
//    public ResponseEntity<SiteDto> createSite(@RequestBody SiteDto siteDto) throws JsonProcessingException {
//        ResponseEntity<SiteDto> response = null;
//        log.info("Site DTO from Controller = " + siteDto.toString());
//        SiteDto createdSite = siteService.createAndGetSite(siteDto);
//
//        if (createdSite != null) {
//            response = new ResponseEntity<>(createdSite, HttpStatus.OK);
//        } else {
//            // Handle the case when site creation fails
//            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return response;
//    }


//        @PostMapping(path = "/site/create")
//    public ResponseEntity<String> createSite(@RequestBody SiteDto siteDto) throws JsonProcessingException {
//        ResponseEntity<String> response = null;
//        log.info("Site DTO from Controller = "+siteDto.toString());
//        SiteDto responseStr = siteService.createAndGetSite(siteDto);
//        response = new ResponseEntity<>(responseStr, org.springframework.http.HttpStatus.OK);
//        return response;
//    }






//    @RequestMapping(value = "/tni/site", method = RequestMethod.GET)
//    public String getSite(@ModelAttribute("siteDto") SiteDto siteDto,Model model) {
//        List<SiteDto> existingSites = siteService.getSites(siteDto);
//        model.addAttribute("sites", existingSites);
//        model.addAttribute("siteDto", siteDto);
//        model.addAttribute("statusList", statusList);
//        model.addAttribute("siteTypeList", siteTypeList);
//        return "new_site";
//    }

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;


    @RequestMapping(value = "/java/api/sendCsvPath", method = RequestMethod.POST, params = "action=getPath")
    public String sendCsvPath(@RequestParam(name = "csvPathRequest") String csvPathRequest, Model model) {
        try {
            log.info("CSV Path Request: " + csvPathRequest);

            String flaskEndpointUrl = "http://localhost:5000/tni/api/sendingCSVPath";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            csvPathRequest = csvPathRequest.replace("\\", "\\\\");
            log.info("CSV Path Request after modify: " + csvPathRequest);

            // Create a request entity with JSON data
            String jsonData = "{\"csvPathRequest\": \"" + csvPathRequest + "\"}";
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonData, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(flaskEndpointUrl, requestEntity, String.class);
            HttpStatus statusCode = response.getStatusCode();
            String responseBody = response.getBody();
            HttpHeaders responseHeaders = response.getHeaders();

            log.info("\n\n\n\n\n\n\nHTTP Status Code:====================================== " + statusCode);
            log.info("\n\n\n\n\nResponse Body:============================================ " + responseBody);
            log.info("\n\n\n\n\nResponse Headers:=============================== " + responseHeaders);

            Map<String, List<SiteDto>> data = dataProcessingService.processJsonData(responseBody);
            model.addAttribute("cluster", data);

            return "dataDisplayPage";
        } catch (IOException e) {
            // Handle the exception, log it, or return an error response as needed.
            e.printStackTrace(); // Example: Print the stack trace
            return "dataDisplayPage"; // You can create a dedicated error page.
        }
    }




    @RequestMapping(value = "/java/api/sendCsvPath")
    public String loadSendCsvPath() {
        return "selectPath";
    }

//    Use RestTemplate to Make a Request
    @RequestMapping(value = "/tni/api", method = RequestMethod.POST)
    public void testConnectionInMakeRequest(){
        String apiUrl = "http://localhost:5000/TestingConnection";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);

        // Process the response
        HttpStatus statusCode = responseEntity.getStatusCode();
        String responseBody = responseEntity.getBody();
        log.info("Response status code: " + statusCode);
        log.info("Response body: " + responseBody);
    }


    @RequestMapping(value = "/TestingConnection2")
    public void testConnectionInWebClient(){
        String apiUrl = "http://localhost:5000/TestingConnection";
        String responseBody = webClientBuilder.build()
                .get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("Response body: " + responseBody);

    }


    @GetMapping("/users/export")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        List<User> listUsers = userService.listAll();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"User ID", "Name", "Enabled", "Roles"};
        String[] nameMapping = {"id", "username", "enabled", "roles"};

        csvWriter.writeHeader(csvHeader);

        for (User user : listUsers) {
            //JavaBean-style getter methods.
            csvWriter.write(user, nameMapping);
        }

        csvWriter.close();

    }

    @RequestMapping(value = "/tni/getCSV", method = RequestMethod.POST, params = "action=getEntity")
    public String getEntityToCSV(@ModelAttribute("entityDto") EntityDto entityDto, HttpServletResponse response,Model model) {
        log.info("GetEntity DTO from Controller = " + entityDto.toString());
        String entity = entityDto.getTableName();
        String csv = "";
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        if (entity.equals("site")) {
            csv = siteService.getSiteToCSV();

            String headerValue = "attachment; filename=sites_" + currentDateTime + ".csv";
            response.setHeader(headerKey, headerValue);
            try (PrintWriter writer = response.getWriter()) {
                writer.write(csv);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if (entity.equals("shelf")) {
            csv = shelfService.getShelfToCSV();

            String headerValue = "attachment; filename=shelves_" + currentDateTime + ".csv";
            response.setHeader(headerKey, headerValue);
            try (PrintWriter writer = response.getWriter()) {
                writer.write(csv);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (entity.equals("slot")) {
            csv = slotService.getSlotToCSV();

            String headerValue = "attachment; filename=slots_" + currentDateTime + ".csv";
            response.setHeader(headerKey, headerValue);
            try (PrintWriter writer = response.getWriter()) {
                writer.write(csv);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (entity.equals("card")) {
            csv = cardService.getCardToCSV();

            String headerValue = "attachment; filename=cards_" + currentDateTime + ".csv";
            response.setHeader(headerKey, headerValue);
            try (PrintWriter writer = response.getWriter()) {
                writer.write(csv);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if (entity.equals("port")) {
            csv = portService.getPortToCSV();

            String headerValue = "attachment; filename=ports_" + currentDateTime + ".csv";
            response.setHeader(headerKey, headerValue);
            try (PrintWriter writer = response.getWriter()) {
                writer.write(csv);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            log.error("Invalid entity name: " + entity);
        }
        log.info("CSV: \n\n" + csv);
        model.addAttribute("result", "Generated CSV Data: " + csv.toString());
        model.addAttribute("entityDto", entityDto);
        model.addAttribute("entityList", entitylist);
        model.addAttribute("downloadList", downloadList);
        return "select_entity_to_csv";
    }

    @RequestMapping(value = "/tni/getCSV")
    public String loadGetEntity(Model model) {
        EntityDto entityDto = new EntityDto();
        log.info("========================="+ entityDto.toString());
        model.addAttribute("entityDto", entityDto);
        model.addAttribute("entityList", entitylist);
        model.addAttribute("downloadList", downloadList);
        return "select_entity_to_csv";
    }
    
    @RequestMapping(value = "/tni/truncate", method = RequestMethod.POST, params = "action=truncate")
    public String truncateTable(@ModelAttribute("truncateDto") EntityDto entityDto, Model model) {
        log.info("Truncate DTO from Controller = " + entityDto.toString());
        String truncateTable = entityDto.getTableName();
        if (truncateTable.equals("site")) {
            siteService.truncateSite();
            shelfService.truncateShelf();
            slotService.truncateSlot();
            cardService.truncateCard();
            portService.truncatePort();
        } else if (truncateTable.equals("shelf")) {
            shelfService.truncateShelf();
            slotService.truncateSlot();
            cardService.truncateCard();
            portService.truncatePort();
        } else if (truncateTable.equals("slot")) {
            slotService.truncateSlot();
            cardService.truncateCard();
            portService.truncatePort();
        } else if (truncateTable.equals("card")) {
            cardService.truncateCard();
            portService.truncatePort();
        } else if (truncateTable.equals("port")) {
            portService.truncatePort();
        } else {
            log.error("Invalid table name: " + truncateTable);
        }
        EntityDto dto = new EntityDto();
        model.addAttribute("entityDto", dto);
        model.addAttribute("entityList", entitylist);
        return "truncate";
    }

    @RequestMapping(value = "/tni/truncate")
    public String loadTruncateTable(Model model) {
        EntityDto entityDto = new EntityDto();
        log.info("Load========================="+ entityDto.toString());
        model.addAttribute("entityDto", entityDto);
        model.addAttribute("entityList", entitylist);
        log.info("Load========================="+ entitylist.toString());
        return "truncate";
    }


    @RequestMapping(value = "/tni/sites",method = RequestMethod.POST, params = "action=DeleteRecords")
    public String deleteSelectedSites(@RequestParam(name = "selectedRecordsIds", required = false) List<Integer> selectedRecordsIds,Model model) {
        log.info("SELECTED SITES FOR DEL=============================="+selectedRecordsIds);
        try{
            siteService.delete(selectedRecordsIds);
        }catch (NullPointerException e){
            log.info("NULL, so no operation was performed");
        }
        SiteDto siteDto = new SiteDto();
        List<SiteDto> sites = siteService.listAll();
        log.info("size of return================="+sites.size());
        model.addAttribute("siteDto", siteDto);
        model.addAttribute("sites",sites);
        return "search_site";
    }

    @RequestMapping(value = "/tni/sites", method = RequestMethod.POST, params = "action=update-site")
    public String updateSite(@ModelAttribute("siteDto") SiteDto siteDto, Model model) {
        log.info("Updating site: " + siteDto.toString());
        SiteDto dto = siteService.updateAndGetSite(siteDto);
        List<SiteDto> sites = new ArrayList<>();
        sites.add(dto);
        model.addAttribute("sites", sites);
    //        model.addAttribute("siteDto", siteDto);
        model.addAttribute("statusList", statusList);
        model.addAttribute("siteTypeList", siteTypeList);
        return "search_site";
    }

    @RequestMapping(value = "/tni/sites", method = RequestMethod.POST, params="action=create-site")
        public String saveSingleSite(@Valid @ModelAttribute("siteDto") SiteDto siteDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statusList", statusList);
            model.addAttribute("siteTypeList", siteTypeList);
            return "new_site";
        }
        log.info("Site DTO from Controller = "+siteDto.toString());
        log.info("FROM FRONTEND====================="+siteDto);
        SiteDto dto = siteService.createAndGetSite(siteDto);
        if (dto == null) {
            model.addAttribute("error", "Site name already exists : " + siteDto.getSiteName());
            return "new_site";
        }
        List<SiteDto> sites = new ArrayList<>();
        sites.add(dto);
        model.addAttribute("sites", sites);
        model.addAttribute("statusList", statusList);
        model.addAttribute("siteTypeList", siteTypeList);
        return "search_site";
    }

    @RequestMapping(value = "/tni/sites", method = RequestMethod.POST, params = "action=search-site")
    public String searchSite(@ModelAttribute("siteDto") SiteDto siteDto, Model model) {
        log.info("Site DTO from Controller = " + siteDto.toString());
        List<SiteDto> sites = siteService.getSites(siteDto);
        log.info("Sites found: " + sites.size());
        if (sites.isEmpty()) {
            model.addAttribute("error", "No sites found.");
        } else {
            model.addAttribute("sites", sites);
        }
        log.info("Site search completed successfully!");
        model.addAttribute("statusList", statusList);
        model.addAttribute("siteTypeList", siteTypeList);
        return "search_site";
    }

    @RequestMapping("/tni/site")
    public String loadSite (Model model) {
        SiteDto siteDto = new SiteDto();
        log.info("Site DTO from Controller = " + siteDto.toString());
        model.addAttribute("siteDto", siteDto);
        model.addAttribute("statusList", statusList);
        model.addAttribute("siteTypeList", siteTypeList);
        return "new_site";
    }

    @RequestMapping(value = "/tni/shelves",method = RequestMethod.POST, params = "action=DeleteRecords")
    public String deleteSelectedShelves(@RequestParam(name = "selectedRecordsIds", required = false) List<Integer> selectedRecordsIds,Model model) {
        log.info("SELECTED SHELVES FOR DEL=============================="+selectedRecordsIds);
        try{
            shelfService.delete(selectedRecordsIds);
        }catch (NullPointerException e){
            log.info("NULL, so no operation was performed");
        }
        ShelfDto shelfDto = new ShelfDto();
        List<ShelfDto> shelfDtoList = shelfService.listAll();
        model.addAttribute("shelfDto", shelfDto);
        model.addAttribute("shelves",shelfDtoList);
        return "search_shelf";
    }
    @RequestMapping(value = "/tni/shelves", method = RequestMethod.POST, params = "action=update-shelf")
    public String updateShelf(@ModelAttribute("shelfDto") ShelfDto shelfDto, Model model) {
        log.info("Updating shelf: " + shelfDto.toString());
        ShelfDto updatedShelfDto = shelfService.updateAndGetShelf(shelfDto);
        List<ShelfDto> shelves = new ArrayList<>();
        shelves.add(updatedShelfDto);
        model.addAttribute("shelves", shelves);
        model.addAttribute("statusList", statusList);
        model.addAttribute("shelfTypeList", shelfTypeList);
        return "search_shelf";
    }

    @RequestMapping(value = "/tni/shelves", method = RequestMethod.POST, params = "action=create-shelf")
    public String saveSingleShelf(@Valid @ModelAttribute("shelfDto") ShelfDto shelfDto,BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statusList", statusList);
            model.addAttribute("shelfTypeList", shelfTypeList);
            return "new_shelf";

        }
        log.info("Shelf DTO from Controller = " + shelfDto.toString());
        ShelfDto dto = shelfService.createAndGetShelf(shelfDto);
        if (dto.getErrorMessage() != null) {
            String errorMessage = dto.getErrorMessage().trim();
            model.addAttribute("error", errorMessage);

            return "new_shelf";
        }
        List<ShelfDto> shelves = new ArrayList<>();
        shelves.add(dto);
        model.addAttribute("shelves", shelves);
        model.addAttribute("statusList", statusList);
        model.addAttribute("shelfTypeList", shelfTypeList);
        return "search_shelf";
    }
    @RequestMapping(value = "/tni/shelves", method = RequestMethod.POST, params = "action=search-shelf")
    public String searchShelf(@ModelAttribute("shelfDto") ShelfDto shelfDto, Model model) {
        log.info("Shelf DTO from Controller = " + shelfDto.toString());
        List<ShelfDto> shelves = shelfService.getShelfs(shelfDto);
        if (shelves.isEmpty()) {
            model.addAttribute("error", "No shelves found.");
        } else {
            model.addAttribute("shelves", shelves);
        }
        log.info("Shelf search completed successfully!");
        model.addAttribute("statusList", statusList);
        model.addAttribute("shelfTypeList", shelfTypeList);
        return "search_shelf";
    }

    @RequestMapping("/tni/shelf")
    public String loadShelf(Model model) {
        ShelfDto shelfDto = new ShelfDto();
        model.addAttribute("shelfDto", shelfDto);
        model.addAttribute("statusList", statusList);
        model.addAttribute("shelfTypeList", shelfTypeList);
        return "new_shelf";
    }

    @RequestMapping(value = "/tni/slots/delete", method = RequestMethod.POST, params = "action=DeleteRecords")
    //we need the required attribute to be false, for the deleteSelectedSlots method to execute even when there is no records selected
    public String deleteSelectedSlots(@RequestParam(name = "selectedRecordsIds", required = false) List<Integer> selectedRecordsIds,Model model) {
        log.info("SLOTS SELECTED=============================="+selectedRecordsIds);
        //this try catch is meant to handel the null pointer exception that is when we receive a empty or a null in place of selected slots
        try{
            slotService.delete(selectedRecordsIds);
        }catch (NullPointerException e){//lets try to give a interface where we provide them a message to select a record to delete
            log.info("NULL, so no operation was performed");
        }
        List<SlotDto> slots = slotService.listAll();
        SlotDto slotDto = new SlotDto();
        //so we are sending two attributes here, one is the slotDto object for the form, and the other is
        // the slots object for the table to generate val
        model.addAttribute("slots", slots);
        model.addAttribute("slotDto", slotDto);
        return "search_slot";
    }
    @RequestMapping(value = "/tni/slots", method = RequestMethod.POST, params = "action=update-slot")
    public String updateSlot(@ModelAttribute("slotDto") SlotDto slotDto, Model model) {
        log.info("Updating slot: " + slotDto.toString());

        SlotDto updatedSlotDto = slotService.updateAndGetSlot(slotDto);
        List<SlotDto> slots = new ArrayList<>();
        slots.add(updatedSlotDto);
        model.addAttribute("slots", slots);
        return "search_slot";
    }

    @RequestMapping(value = "/tni/slots", method = RequestMethod.POST, params = "action=create-slot")
    public String saveSingleSlot(@Valid @ModelAttribute("slotDto") SlotDto slotDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "new_slot";
        }
        log.info("Slot DTO from Controller = " + slotDto.toString());
        SlotDto dto = slotService.createSingleSlot(slotDto);
        if (dto.getErrorMessage() != null) {
            String errorMessage = dto.getErrorMessage().trim();
            model.addAttribute("error", errorMessage);
            return "new_slot";
        }
        List<SlotDto> slots = new ArrayList<>();
        slots.add(dto);
        model.addAttribute("slots", slots);
        return "search_slot";
    }
    @RequestMapping(value = "/tni/slots", method = RequestMethod.POST, params = "action=search-slot")
    public String searchSlot(@ModelAttribute("slotDto") SlotDto slotDto, Model model) {
        log.info("Slot DTO from Controller = " + slotDto.toString());
        List<SlotDto> slots = slotService.getSlots(slotDto);
        if (slots.isEmpty()) {
            model.addAttribute("error", "No slots found.");
        } else {
            model.addAttribute("slots", slots);
        }
        log.info("Slot search completed successfully!");
        return "search_slot";
    }
    @RequestMapping("/tni/slot")
    public String loadSlot (Model model) {
        SlotDto slotDto = new SlotDto();
        model.addAttribute("slotDto", slotDto);
        return "new_slot";
    }
    @RequestMapping(value = "/tni/cards/delete", method = RequestMethod.POST, params = "action=DeleteRecords")
    public String deleteSelectedCards(@RequestParam(name = "selectedRecordsIds", required = false) List<Integer> selectedRecordsIds,Model model) {
        log.info("SELECTED CARDS FOR DEL=============================="+selectedRecordsIds);
        try{
            cardService.delete(selectedRecordsIds);
        }catch (NullPointerException e){
            log.info("NULL, so no operation was performed");
        }
        CardDto cardDto = new CardDto();
        List<CardDto> cards = cardService.listAll();
        //so we are sending two attributes here, one is the cardDto object for the form, and the other is
        // the cards object for the table to generate val
        model.addAttribute("cardDto", cardDto);
        model.addAttribute("cards",cards);
        return "search_card";
    }
    @RequestMapping(value = "/tni/cards", method = RequestMethod.POST, params = "action=update-card")
    public String updateCard(@ModelAttribute("cardDto") CardDto cardDto, Model model) {
        log.info("Updating card: " + cardDto.toString());

        CardDto updatedCardDto = cardService.updateAndGetCard(cardDto);

        List<CardDto> cards = new ArrayList<>();
        cards.add(updatedCardDto);
        model.addAttribute("cards", cards);
        model.addAttribute("statusList", statusList);
        model.addAttribute("shelfTypeList", shelfTypeList);
        return "search_card";
    }

    @RequestMapping(value = "/tni/cards", method = RequestMethod.POST, params = "action=create-card")
    public String saveSingleCard(@Valid @ModelAttribute("cardDto") CardDto cardDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "new_card";
        }
        log.info("Card DTO from Controller = " + cardDto.toString());
        CardDto dto = cardService.createSingleCard(cardDto);
        if (dto == null) {
            model.addAttribute("error", "Card name already exists: " + cardDto.getCardName());
            return "new_card";
        }
        List<CardDto> cards = new ArrayList<>();
        cards.add(dto);
        model.addAttribute("cards", cards);

        model.addAttribute("statusList", statusList);
        model.addAttribute("shelfTypeList", shelfTypeList);
        return "search_card";
    }

    @RequestMapping(value = "/tni/cards", method = RequestMethod.POST, params = "action=search-card")
    public String searchCard(@ModelAttribute("cardDto") CardDto cardDto, Model model) {
        log.info("Card DTO from Controller = " + cardDto.toString());
        List<CardDto> cards = cardService.getCards(cardDto);
        if (cards.isEmpty()) {
            model.addAttribute("error", "No cards found.");
        } else {
            model.addAttribute("cards", cards);
        }
        log.info("Card search completed successfully!");

        model.addAttribute("statusList", statusList);
        model.addAttribute("shelfTypeList", shelfTypeList);
        return "search_card";
    }

    @RequestMapping("/tni/card")
    public String loadCard(Model model) {
        CardDto cardDto = new CardDto();
        model.addAttribute("cardDto", cardDto);
        model.addAttribute("statusList", statusList);
        model.addAttribute("shelfTypeList", shelfTypeList);
        return "new_card";
    }

    @RequestMapping(value = "/tni/ports/delete", method = RequestMethod.POST, params = "action=DeleteRecords")
    public String deleteSelectedPorts(@RequestParam(name = "selectedRecordsIds", required = false) List<Integer> selectedRecordsIds,Model model) {
        log.info("SELECTED PORTS FOR DEL=============================="+selectedRecordsIds);
        try{
            portService.delete(selectedRecordsIds);
        }catch (NullPointerException e){
            log.info("NULL, so no operation was performed");
        }
        PortDto portDto = new PortDto();
        List<PortDto> ports = portService.listAll();
        model.addAttribute("portDto", portDto);
        model.addAttribute("ports",ports);
        model.addAttribute("portTypeList",portTypeList);
        model.addAttribute("portBandwidthList",portBandwidthList);
        return "search_port";
    }

    @RequestMapping(value = "/tni/ports", method = RequestMethod.POST, params = "action=update-port")
    public String updatePort(@ModelAttribute("portDto") PortDto portDto, Model model) {
        log.info("Updating port: " + portDto.toString());
        PortDto updatedPortDto = portService.updateAndGetPort(portDto);

        List<PortDto> ports = new ArrayList<>();
        ports.add(updatedPortDto);
        model.addAttribute("ports", ports);
        model.addAttribute("portTypeList",portTypeList);
        model.addAttribute("portBandwidthList",portBandwidthList);
        return "search_port";
    }

    @RequestMapping(value = "/tni/ports", method = RequestMethod.POST, params = "action=create-port")
    public String saveSinglePort(@Valid @ModelAttribute("portDto") PortDto portDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "new_port";
        }
        log.info("Port DTO from Controller = " + portDto.toString());
        PortDto dto = portService.createSinglePort(portDto);
        if (dto == null) {
            model.addAttribute("error", "Port name already exists: " + portDto.getPortName());
            return "new_port";
        }
        List<PortDto> ports = new ArrayList<>();
        ports.add(dto);
        model.addAttribute("ports", ports);
        model.addAttribute("portTypeList",portTypeList);
        model.addAttribute("portBandwidthList",portBandwidthList);
        return "search_port";
    }

    @RequestMapping(value = "/tni/ports", method = RequestMethod.POST, params = "action=search-port")
    public String searchPort(@ModelAttribute("portDto") PortDto portDto, Model model) {
        log.info("Port DTO from Controller = " + portDto.toString());
        List<PortDto> ports = portService.getPorts(portDto);
        if (ports.isEmpty()) {
            model.addAttribute("error", "No ports found.");
        } else {
            model.addAttribute("ports", ports);
        }
        log.info("Port search completed successfully!");
        model.addAttribute("portTypeList",portTypeList);
        model.addAttribute("portBandwidthList",portBandwidthList);
        return "search_port";
    }

    @RequestMapping("/tni/port")
    public String loadPort(Model model) {
        PortDto portDto = new PortDto();
        model.addAttribute("portDto", portDto);
        model.addAttribute("portTypeList",portTypeList);
        model.addAttribute("portBandwidthList",portBandwidthList);
        return "new_port";
    }

    @RequestMapping("/tni/loadMaster")
    public String loadMaster (Model model) {
        log.info("From /load-master");
        return "load_master";
    }
    @RequestMapping(value = "/loadMaster", method = RequestMethod.POST)
    public ModelAndView loadMasterData(@RequestParam("master") MultipartFile file) {
        ModelAndView modelAndView = new ModelAndView("load_master");
        if (file.isEmpty()) { String errorMessage = "Please choose a file to upload."; modelAndView.addObject("msg1", errorMessage);
            log.error(errorMessage); return modelAndView;}
        try {
            List<Object> masterList = masterService.loadAndSaveData(file);
            boolean hasErrors = false;
            for (Object data : masterList) {
                if (data instanceof String) {
                    String errorMessage = (String) data;
                    modelAndView.addObject("msg1", errorMessage);
                    log.error(errorMessage);
                    hasErrors = true;
                    break;
                }
            }
            if (hasErrors) {
                return modelAndView; // Stop execution and return error message
            } else {
                modelAndView.addObject("msg2", "Master data saved successfully!");
                log.info("Master data saved successfully!");
            }
            modelAndView.addObject("masterList", masterList);
            return modelAndView;
        } catch (DataProcessingException e) {
            String errorMessage = e.getMessage();
            modelAndView.addObject("msg1", errorMessage);
            log.error(errorMessage);
            modelAndView.addObject("errorStackTrace", ""); // Empty stack trace
            return modelAndView;
        }
    }

    @RequestMapping(value = "/allUsers",method = RequestMethod.GET)
    public  String viewUsers(@ModelAttribute("userDto") UserDto userDto, Model model){
//        List<UserDto> users = userService.getUser(userDto);
        Iterable<User> users = userService.users(userDto);
        model.addAttribute("users",users);
//    model.addAttribute("")
        return "all_users";
    }
    @RequestMapping(value = "/newUser",method = RequestMethod.POST,params = "action=create-user")
    public  String addUser(@ModelAttribute("userDto") UserDto userDto, Model model){
        userDto.setCreatedDate(new Date());
        userDto.setPassword(generateEncodedPassword(userDto.getPassword()));
        userService.addUser(userDto);
        return "redirect:/allUsers";
    }
    private String generateEncodedPassword(String pwd){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(pwd);
    }

    @RequestMapping(value = "/newUser")
    public  String loadUser( Model model){
//        List<UserDto> users = userService.getUser(userDto);
        UserDto userDto =  new UserDto();
        List<Role> roles = (List<Role>) roleRepository.findAll();

        model.addAttribute("roles",roles);
        model.addAttribute("userDto",userDto);
////    model.addAttribute("")
//        return "all_users";
        return  "new_user";
    }

    @RequestMapping(value = "/processUser/{id}", method =RequestMethod.POST, params="action=enable")
    public ModelAndView processUserEnable(@PathVariable (name="id") Long id) {
        User user = userService.get(id);
        user.setEnabled(true);
        userService.save(user);
        ModelAndView mav = new ModelAndView("all_users");
        List<User> users = userService.listAll();
        mav.addObject("users",users);
        return mav;
    }

    @RequestMapping(value = "/processUser/{id}", method =RequestMethod.POST, params="action=disable")
    public ModelAndView processUserDisable(@PathVariable (name="id") Long id) {
        User user = userService.get(id);
        user.setEnabled(false);
        userService.save(user);
        ModelAndView mav = new ModelAndView("all_users");
        List<User> users = userService.listAll();
        mav.addObject("users",users);
        return mav;
    }

    @RequestMapping(value = "/processUser/{id}", method =RequestMethod.POST, params="action=delete")
    public ModelAndView processUserDelete(@PathVariable (name="id") Long id) {
        userService.delete(id);
        ModelAndView mav = new ModelAndView("all_users");
        List<User> users = userService.listAll();
        mav.addObject("users",users);
        return mav;
    }

    @RequestMapping("/tni")
    public String viewHomePage(Model model) throws JsonProcessingException {
        log.info("Index");
        return "index";
    }

}
