package aibg.logika.service;

import aibg.logika.dto.*;

public interface GameService {

    DTO startGameState(GameStateRequestDTO dto);

    DTO playerView(PlayerViewRequestDTO dto);

    DTO doAction(DoActionRequestDTO dto);

    DTO removePlayer(RemovePlayerRequestDTO dto);

    DTO startTrainGameState(TrainGameStateRequestDTO dto);

    DTO train(TrainRequestDTO dto);

    DTO doActionTrain(Integer gameId, Integer playerIdx, String action);

    DTO watchGame(WatchGameRequestDTO dto);
}
