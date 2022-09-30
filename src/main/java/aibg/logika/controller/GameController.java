package aibg.logika.controller;

import aibg.logika.dto.*;
import aibg.logika.service.GameService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Getter
@Setter
@RequestMapping("/logic")
public class GameController {

    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {this.gameService = gameService;}

    @PostMapping ("/getStartGameState")
    public ResponseEntity<DTO> startGameState(@RequestBody GameStateRequestDTO dto) {
        DTO response = gameService.startGameState(dto);

        if(response instanceof GameStateResponseDTO) {
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/getPlayerView")
    public ResponseEntity<DTO> playerView(@RequestBody PlayerViewRequestDTO dto) {
        DTO response = gameService.playerView(dto);

        if(response instanceof PlayerViewResponseDTO) {
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/doAction")
    public ResponseEntity<DTO> doAction(@RequestBody DoActionRequestDTO dto) {
        DTO response = gameService.doAction(dto);

        if(response instanceof DoActionResponseDTO) {
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/removePlayer")
    public ResponseEntity<DTO> removePlayer(@RequestBody RemovePlayerRequestDTO dto) {
        DTO response = gameService.removePlayer(dto);

        if(response instanceof RemovePlayerResponseDTO) {
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }
}
