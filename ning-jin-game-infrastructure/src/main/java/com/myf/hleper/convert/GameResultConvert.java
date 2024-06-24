package com.myf.hleper.convert;

import com.myf.common.enmus.GameTypeEnum;
import com.myf.common.util.BooleanLogicExecuteUtils;
import com.myf.hleper.model.dto.BoardDTO;
import com.myf.hleper.model.dto.GameResultDTO;
import com.myf.zouding.database.entity.GameResultDO;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: myf
 * @CreateTime: 2024-05-23  21:50
 * @Description: GameResultConvert
 */
public class GameResultConvert {


    public static GameResultDTO Board2GameResultDTO(BoardDTO board){
        GameResultDTO gameResultDTO =new GameResultDTO();
        if(StringUtils.isNotBlank(board.getUserId())){
            //winner是当前用户
            gameResultDTO.setWinnerId(board.getUserId());
        }
        if(StringUtils.isNotBlank(board.getGameType())){
            gameResultDTO.setGameType(board.getGameType());
            /*if(GameTypeEnum.INVITE.name().equals(board.getGameType())){
                BooleanLogicExecuteUtils.execute(
                        "INVITER".equals(board.getInviteType()),
                        () -> inviterSetData(gameResultDTO,board),
                        () -> invitedSetData(gameResultDTO,board)
                );
            }else {
                inviterSetData(gameResultDTO,board);
            }*/
            //保持player1位胜利者逻辑 player2为失败者
            inviterSetData(gameResultDTO,board);
        }else{
            inviterSetData(gameResultDTO,board);
        }

        return gameResultDTO;
    }


    private static void inviterSetData(GameResultDTO gameResultDTO,BoardDTO board){
        if(StringUtils.isNotBlank(board.getUserId())){
            gameResultDTO.setPlayer1Id(board.getUserId());
        }
        if(StringUtils.isNotBlank(board.getUserName())){
            gameResultDTO.setPlayer1Name(board.getUserName());
        }
        if(StringUtils.isNotBlank(board.getOtherUserId())){
            gameResultDTO.setPlayer2Id(board.getOtherUserId());
        }
        if(StringUtils.isNotBlank(board.getOtherUserName())){
            gameResultDTO.setPlayer2Name(board.getOtherUserName());
        }
    }

    private static void invitedSetData(GameResultDTO gameResultDTO,BoardDTO board){
        if(StringUtils.isNotBlank(board.getUserId())){
            gameResultDTO.setPlayer2Id(board.getUserId());
        }
        if(StringUtils.isNotBlank(board.getUserName())){
            gameResultDTO.setPlayer2Name(board.getUserName());
        }
        if(StringUtils.isNotBlank(board.getOtherUserId())){
            gameResultDTO.setPlayer1Id(board.getOtherUserId());
        }
        if(StringUtils.isNotBlank(board.getOtherUserName())){
            gameResultDTO.setPlayer1Name(board.getUserName());
        }
    }

    public static GameResultDO DTO2DO(GameResultDTO gameResultDTO){
        GameResultDO gameResultDO =new GameResultDO();
        if(StringUtils.isNotBlank(gameResultDTO.getWinnerId())){
            gameResultDO.setWinnerId(gameResultDTO.getWinnerId());
        }
        if(StringUtils.isNotBlank(gameResultDTO.getGameType())){
            gameResultDO.setGameType(gameResultDTO.getGameType());
        }
        if(StringUtils.isNotBlank(gameResultDTO.getPlayer1Id())){
            gameResultDO.setPlayer1Id(gameResultDTO.getPlayer1Id());
        }
        if(StringUtils.isNotBlank(gameResultDTO.getPlayer1Name())){
            gameResultDO.setPlayer1Name(gameResultDTO.getPlayer1Name());
        }
        if(StringUtils.isNotBlank(gameResultDTO.getPlayer2Id())){
            gameResultDO.setPlayer2Id(gameResultDTO.getPlayer2Id());
        }
        if(StringUtils.isNotBlank(gameResultDTO.getPlayer2Name())){
            gameResultDO.setPlayer2Name(gameResultDTO.getPlayer2Name());
        }
        return gameResultDO;
    }
}
