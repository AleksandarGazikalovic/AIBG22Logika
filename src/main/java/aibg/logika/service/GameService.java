package aibg.logika.service;

import aibg.logika.dto.DTO;
import aibg.logika.dto.PlayerViewRequestDTO;
import aibg.logika.dto.DoActionRequestDTO;
import aibg.logika.dto.RemovePlayerRequestDTO;

public interface GameService {

    DTO startGameState();

    DTO playerView(PlayerViewRequestDTO dto);

    DTO doAction(DoActionRequestDTO dto);

    DTO removePlayer(RemovePlayerRequestDTO dto);
}
