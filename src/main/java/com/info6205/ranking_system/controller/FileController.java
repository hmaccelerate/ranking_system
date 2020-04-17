package com.info6205.ranking_system.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.info6205.ranking_system.Service.FileService;
import com.info6205.ranking_system.model.Match;
import com.info6205.ranking_system.model.Team;
import com.info6205.ranking_system.util.ReadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class FileController {

    @Autowired
    FileService fileService;

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        String path=fileService.uploadFile(file);
        List<Match> matches = new ArrayList<>();
        Map<String, Team> teamsMap = new HashMap<>();
        ReadUtil.readFromCSV(matches,path);
        Simulator simulator=new Simulator();
        simulator.simulateEPL(matches,teamsMap,2000,"normal",50);
        List<Team> orderedTeam= simulator.rankingTeams(teamsMap);
        redirectAttributes.addFlashAttribute("allTeams",orderedTeam);
//        redirectAttributes.addFlashAttribute("message",
//                "You successfully uploaded " + orderedTeam.get(1).getTeamName() + "!");

        return "redirect:/rankingTeam";
    }

    @RequestMapping(value ="/rankingTeam", method = RequestMethod.GET)
    public String rankingTeam(){
        return "ranking";
    }
}