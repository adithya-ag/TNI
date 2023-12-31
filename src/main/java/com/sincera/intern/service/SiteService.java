package com.sincera.intern.service;

import com.opencsv.CSVWriter;
import com.sincera.intern.dto.SiteDto;

//import com.sincera.intern.mapper.SiteMapper;
import com.sincera.intern.model.Site;
//import com.sincera.intern.mapper.SiteMapper;
import com.sincera.intern.repository.SiteRepository;
import com.sincera.intern.util.SiteValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SiteService {

    private static final Logger log = LoggerFactory.getLogger(SiteService.class);

    @Autowired
    SiteRepository siteRepository;
//    @Autowired
//    SiteMapper siteMapper;
    //hey there this is tech
    //testing the branch

    public SiteDto createAndGetSite(SiteDto siteDto) {

        String response = "";

        String validationResult = SiteValidation.validateSiteCreate(siteDto);

        if (validationResult != null && validationResult.equals("OK")) {

            boolean data = siteRepository.getSitesFromName(siteDto.getSiteName()).isEmpty();

            if (data) {

                Site site = new Site();

                site.setSiteName(siteDto.getSiteName());
                site.setSiteType(siteDto.getSiteType());
                site.setStatus(siteDto.getStatus());
                site.setLatitude(siteDto.getLatitude());
                site.setLongitude(siteDto.getLongitude());
                site.setAddress1(siteDto.getAddress1());
                site.setAddress2(siteDto.getAddress2());
                site.setCity(siteDto.getCity());
                site.setState(siteDto.getState());
                site.setCountry(siteDto.getCountry());
                site.setPin(siteDto.getPin());
                site.setCreatedAt(LocalDate.now());
                site.setLastModifiedAt(LocalDate.now());
                site.setCreatedBy(siteDto.getCreatedBy());

                log.info("Site to be created = " + site);

                siteRepository.save(site);

                response = "Site Created Successfully with data \n" + site;
                log.info("=============================="+response);


                Site siteCreated = getSite(siteDto);
                if (siteCreated != null) {
                    SiteDto dto = new SiteDto();
                    dto.setSiteId(siteCreated.getSiteId());
                    dto.setSiteName(siteCreated.getSiteName());
                    dto.setStatus(siteCreated.getStatus());
                    dto.setSiteType(siteCreated.getSiteType());
                    dto.setLatitude(site.getLatitude());
                    dto.setLongitude(site.getLongitude());
                    dto.setAddress1(site.getAddress1());
                    dto.setAddress2(site.getAddress2());
                    dto.setCity(site.getCity());
                    dto.setState(site.getState());
                    dto.setCountry(site.getCountry());
                    dto.setPin(site.getPin());
                    return dto;
                }

            } else {
                siteDto.setErrorMessage("Site name already exists: " + siteDto.getSiteName());
                //throw new NameAlreadyExistException("User already exists with this name " + siteDto.getSiteName());
            }
        }
        return null;
    }

    public Site getSite(SiteDto siteDto) {
        List<Site> sites = siteRepository.getSitesFromName(siteDto.getSiteName());
        if (null != sites) {
            return sites.get(0);
        }
        return null;
    }

    public List<SiteDto> getSites(SiteDto siteDto) {
        Integer Id = siteDto.getSiteId();
        String name = siteDto.getSiteName();
        String type = siteDto.getSiteType();
        String status = siteDto.getStatus();
        String latitude = siteDto.getLatitude();
        String longitude = siteDto.getLongitude();
        String address1 = siteDto.getAddress1();
        String address2 = siteDto.getAddress2();
        String city = siteDto.getCity();
        String state = siteDto.getState();
        String country = siteDto.getCountry();
        Integer pin = siteDto.getPin();
        String createdBy = siteDto.getCreatedBy();
        LocalDate createdAt = siteDto.getCreatedAt();
        LocalDate lastModifiedAt = siteDto.getLastModifiedAt();

        return convertToSiteDtoList(siteRepository.getSitesBy(Id, name, status, type, latitude, longitude, address1, address2, city, state, country, pin, createdBy, createdAt, lastModifiedAt));
    }


    private List<SiteDto> convertToSiteDtoList(List<Site> sites) {
        List<SiteDto> siteDtoList = new ArrayList<>();
        if (sites != null && !sites.isEmpty()) {
            for (Site site : sites) {
                SiteDto dto = new SiteDto();
                dto.setSiteName(site.getSiteName());
                dto.setSiteId(site.getSiteId());
                dto.setSiteType(site.getSiteType());
                dto.setStatus(site.getStatus());
                dto.setLatitude(site.getLatitude());
                dto.setLongitude(site.getLongitude());
                dto.setAddress1(site.getAddress1());
                dto.setAddress2(site.getAddress2());
                dto.setCity(site.getCity());
                dto.setState(site.getState());
                dto.setCountry(site.getCountry());
                dto.setPin(site.getPin());
                dto.setCreatedBy(site.getCreatedBy());
                dto.setCreatedAt(site.getCreatedAt());
                dto.setLastModifiedAt(site.getLastModifiedAt());
                siteDtoList.add(dto);
            }
        }
        return siteDtoList;
    }

    public SiteDto updateAndGetSite(SiteDto siteDto) {

        boolean data = !(siteRepository.getSitesFromName(siteDto.getSiteName()).isEmpty());

        if (data) {
            Site site = siteRepository.getSiteBySiteName(siteDto.getSiteName());
            if (site != null) {
                site.setSiteId(siteDto.getSiteId());
                site.setSiteType(siteDto.getSiteType());
                site.setStatus(siteDto.getStatus());
                site.setLatitude(siteDto.getLatitude());
                site.setLongitude(siteDto.getLongitude());
                site.setAddress1(siteDto.getAddress1());
                site.setAddress2(siteDto.getAddress2());
                site.setCity(siteDto.getCity());
                site.setState(siteDto.getState());
                site.setCountry(siteDto.getCountry());
                site.setPin(siteDto.getPin());

                siteRepository.save(site);
                }
            Site siteUpdated = getSite(siteDto);
            if (siteUpdated != null) {
                SiteDto dto = new SiteDto();
                dto.setSiteName(siteUpdated.getSiteName());
                dto.setSiteId(siteUpdated.getSiteId());
                dto.setSiteType(siteUpdated.getSiteType());
                dto.setStatus(siteUpdated.getStatus());
                dto.setLatitude(siteUpdated.getLatitude());
                dto.setLongitude(siteUpdated.getLongitude());
                dto.setAddress1(siteUpdated.getAddress1());
                dto.setAddress2(siteUpdated.getAddress2());
                dto.setCity(siteUpdated.getCity());
                dto.setState(siteUpdated.getState());
                dto.setCountry(siteUpdated.getCountry());
                dto.setPin(siteUpdated.getPin());
                dto.setCreatedAt(siteUpdated.getCreatedAt());
                dto.setCreatedBy(siteUpdated.getCreatedBy());

                return dto;
            }

        } else {
            throw new IllegalArgumentException("No site found with the provided name");
        }
        return null;
    }
    public void truncateSite() {
        siteRepository.truncateSite();
    }

    public void delete(List<Integer> selectedRecordsIds) {
        for (Integer id : selectedRecordsIds) {
            siteRepository.deleteById(id);
        }
    }

    public List<SiteDto> listAll() {
        return convertToSiteDtoList((List<Site>) siteRepository.findAll());
    }

    public String getSiteToCSV() {
        List<Site> data = (List<Site>) siteRepository.findAll(); // Fetch data from the repository

        // Convert data to CSV format
        StringWriter writer = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            String[] header = { "siteId", "siteName","status", "siteType","latitude", "longitude","address1", "address2","city", "state","country", "pin" }; // Replace with your column names
            csvWriter.writeNext(header);

            for (Site entity : data) {
                String[] row = {String.valueOf(entity.getSiteId()), entity.getSiteName(), entity.getStatus(), entity.getSiteType(),entity.getLatitude(),entity.getLongitude(), entity.getAddress1(), entity.getAddress2() , entity.getCity(),entity.getState(), entity.getCountry(), String.valueOf(entity.getPin())}; // Replace with your entity fields
                csvWriter.writeNext(row);
            }
        } catch (IOException e) {
            // Handle the exception
        }


        log.info("CSV DATA==============================\n\n"+writer.toString());
        String csvData = writer.toString();

        // You can return the CSV data or save it to a file, depending on your use case
        return csvData;
    }
}