package com.myf.hleper.model.dto;

import com.myf.common.enmus.UserStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: myf
 * @CreateTime: 2023-12-16  18:03
 * @Description: UserDTO
 */
@NoArgsConstructor
@Data
public class UserDTO {

    private String userId;

    private volatile UserStatusEnum userStatusEnum;

    private String userName;

    private String avatarUrl;

    private String otherUserId;

    private String otherCell;

    private String otherUserName;

    private String otherAvatarUrl;

    private String cell;

    private String gameType;

    private Integer winCount;

    private Integer loseCount;

    private Integer otherWinCount;

    private Integer otherLoseCount;

    /**
     * 邀请类型 inviter 邀请者 invited 被邀请者
     */
    private String inviteType;

    /**
     * 阶数
     */
    private Integer columnNumber;

    private CellDTO[][] initBoards;


    public UserDTO(String userId, UserStatusEnum userStatusEnum, String userName, String otherUserId, String otherCell, String otherUserName, String cell) {
        this.userId = userId;
        this.userStatusEnum = userStatusEnum;
        this.userName = userName;
        this.otherUserId = otherUserId;
        this.otherCell = otherCell;
        this.otherUserName = otherUserName;
        this.cell = cell;
    }

    public void buildFourBoard(String currentSide,Integer columnNumber){
        initBoards =new CellDTO[columnNumber][columnNumber];
        for(int i = 0;i<columnNumber;i++){
            for(int j = 0; j<columnNumber;j++){
                initBoards[i][j] = new CellDTO();
                initBoards[i][j].setColor("#282c34");
                if(i == 0){
                    initBoards[i][j].setContent("X".equals(currentSide) ? "O" : "X");
                }else if(i == columnNumber - 1){
                    initBoards[i][j].setContent("O".equals(currentSide) ? "O" : "X");
                    if("O".equals(currentSide)){
                        initBoards[i][j].setClickable(true);
                        continue;
                    }
                }
                initBoards[i][j].setClickable(false);
            }
        }
    }
}
