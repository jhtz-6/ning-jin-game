package com.myf.hleper.model.dto;

import com.myf.common.util.BooleanLogicExecuteUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: myf
 * @CreateTime: 2023-12-13  22:09
 * @Description: TODO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CellDTO {

    private String content;

    private String color;

    private String name;

    private Boolean clickable;

    private Integer row;

    private Integer column;


    public static CellDTO[][] getXBoards(Integer columnNum){
        return BooleanLogicExecuteUtils.executeWithResult(4 == columnNum , CellDTO::getFourXBoards,
                CellDTO::getFiveXBoards);
    }

    public static CellDTO[][] getOBoards(Integer columnNum){
        return BooleanLogicExecuteUtils.executeWithResult(4 == columnNum , CellDTO::getFourOBoards,
                CellDTO::getFiveOBoards);
    }

    public static CellDTO[][] getFourXBoards(){

        CellDTO[][] cellDTOS = new CellDTO[4][4];
        cellDTOS[0][0]=new CellDTO("X","#282c34","Chess1x",true,null,null);
        cellDTOS[0][1]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[0][2]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[0][3]=new CellDTO("O","#282c34","Chess5o",false,null,null);

        cellDTOS[1][0]=new CellDTO("X","#282c34","Chess2x",true,null,null);
        cellDTOS[1][1]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[1][2]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[1][3]=new CellDTO("O","#282c34","Chess6o",false,null,null);

        cellDTOS[2][0]=new CellDTO("X","#282c34","Chess3x",true,null,null);
        cellDTOS[2][1]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[2][2]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[2][3]=new CellDTO("O","#282c34","Chess7o",false,null,null);

        cellDTOS[3][0]=new CellDTO("X","#282c34","Chess4x",true,null,null);
        cellDTOS[3][1]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[3][2]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[3][3]=new CellDTO("O","#282c34","Chess8o",false,null,null);
        return cellDTOS;
    }

    public static CellDTO[][] getFourOBoards(){

        CellDTO[][] cellDTOS = new CellDTO[4][4];
        cellDTOS[0][0]=new CellDTO("O","#282c34","Chess5o",false,null,null);
        cellDTOS[0][1]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[0][2]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[0][3]=new CellDTO("X","#282c34","Chess1x",false,null,null);

        cellDTOS[1][0]=new CellDTO("O","#282c34","Chess6o",false,null,null);
        cellDTOS[1][1]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[1][2]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[1][3]=new CellDTO("X","#282c34","Chess2x",false,null,null);

        cellDTOS[2][0]=new CellDTO("O","#282c34","Chess7o",false,null,null);
        cellDTOS[2][1]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[2][2]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[2][3]=new CellDTO("X","#282c34","Chess3x",false,null,null);

        cellDTOS[3][0]=new CellDTO("O","#282c34","Chess8o",false,null,null);
        cellDTOS[3][1]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[3][2]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[3][3]=new CellDTO("X","#282c34","Chess4x",false,null,null);
        return cellDTOS;
    }

    public static CellDTO[][] getFiveXBoards(){

        CellDTO[][] cellDTOS = new CellDTO[5][5];
        cellDTOS[0][0]=new CellDTO("X","#282c34","Chess1x",true,null,null);
        cellDTOS[0][1]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[0][2]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[0][3]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[0][4]=new CellDTO("O","#282c34","Chess6o",false,null,null);

        cellDTOS[1][0]=new CellDTO("X","#282c34","Chess2x",true,null,null);
        cellDTOS[1][1]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[1][2]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[1][3]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[1][4]=new CellDTO("O","#282c34","Chess7o",false,null,null);

        cellDTOS[2][0]=new CellDTO("X","#282c34","Chess3x",true,null,null);
        cellDTOS[2][1]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[2][2]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[2][3]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[2][4]=new CellDTO("O","#282c34","Chess8o",false,null,null);

        cellDTOS[3][0]=new CellDTO("X","#282c34","Chess4x",true,null,null);
        cellDTOS[3][1]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[3][2]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[3][3]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[3][4]=new CellDTO("O","#282c34","Chess9o",false,null,null);


        cellDTOS[4][0]=new CellDTO("X","#282c34","Chess5x",true,null,null);
        cellDTOS[4][1]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[4][2]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[4][3]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[4][4]=new CellDTO("O","#282c34","Chess10o",false,null,null);
        return cellDTOS;
    }

    public static CellDTO[][] getFiveOBoards(){

        CellDTO[][] cellDTOS = new CellDTO[5][6];
        cellDTOS[0][0]=new CellDTO("O","#282c34","Chess6o",false,null,null);
        cellDTOS[0][1]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[0][2]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[0][3]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[0][4]=new CellDTO("X","#282c34","Chess1x",false,null,null);

        cellDTOS[1][0]=new CellDTO("O","#282c34","Chess7o",false,null,null);
        cellDTOS[1][1]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[1][2]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[1][3]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[1][4]=new CellDTO("X","#282c34","Chess2x",false,null,null);

        cellDTOS[2][0]=new CellDTO("O","#282c34","Chess8o",false,null,null);
        cellDTOS[2][1]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[2][2]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[2][3]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[2][4]=new CellDTO("X","#282c34","Chess3x",false,null,null);

        cellDTOS[3][0]=new CellDTO("O","#282c34","Chess9o",false,null,null);
        cellDTOS[3][1]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[3][2]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[3][3]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[3][4]=new CellDTO("X","#282c34","Chess4x",false,null,null);


        cellDTOS[4][0]=new CellDTO("O","#282c34","Chess10o",false,null,null);
        cellDTOS[4][1]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[4][2]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[4][3]=new CellDTO("","#282c34","",true,null,null);
        cellDTOS[4][4]=new CellDTO("X","#282c34","Chess5x",false,null,null);
        return cellDTOS;
    }
}
