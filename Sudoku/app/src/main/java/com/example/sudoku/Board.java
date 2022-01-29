package com.example.sudoku;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Board {
    private int N;
    private int [][] starter;
    private int SRN;
    private int K;

    public Board(){
        this.N = 9;
        this.SRN = (int) Math.sqrt(N);
        this.K = 12;
        this.starter = new int [9][9];



    }

    private void fillDiagonal(){
        for(int i = 0; i < this.N; i = i + this.SRN){
            this.fillBox(i,i);
        }
    }

    private boolean unUsedInBox(int rowStart, int colStart, int num, boolean verification){
        int count = 0;
        for(int i = 0; i < this.SRN; i++){
            for(int j = 0; j < this.SRN; j ++){
                if(this.starter[rowStart + i][colStart + j] == num){
                    count ++;
                }
            }
        }
        if(verification){
            return count == 1;
        }
        return count < 1;
    }

    private void fillBox(int row, int col){
        int num;
        for(int i = 0; i < this.SRN; i ++){
            for(int j = 0; j < this.SRN; j ++){
                do{
                    num = this.randomGenerator(this.N);
                } while(!this.unUsedInBox(row, col, num, false));
                this.starter[row + i][col + j] = num;
            }
        }
    }

    private int randomGenerator( int num){
        return (int) Math.floor(Math.random() *num + 1);
    }

    private boolean CheckIfSafe(int i, int j, int num, boolean verification){
        return this.unUsedInRow(i, num, verification) &&
                this.unUsedInCol(j, num, verification) &&
                this.unUsedInBox(i - i % this.SRN, j - j % this.SRN, num, verification);
    }

    private boolean CheckIfSafeVerify(int i, int j, int num, boolean verification){
        return this.unUsedInRow(i, num, verification) ||
                this.unUsedInCol(j, num, verification) ||
                this.unUsedInBox(i - i % this.SRN, j - j % this.SRN, num, verification);
    }

    private boolean unUsedInCol(int j, int num, boolean verification) {
        int count = 0;
        for(int i = 0; i < this.N; i++){
            if(this.starter[i][j] == num){
                count ++;
            }
        }
        if(verification){
            return count == 1;
        }
        return count < 1;
    }

    private boolean unUsedInRow(int i, int num, boolean verification) {
        int count = 0;
        for(int j = 0; j < this.N; j++){
            if(this.starter[i][j] == num){
                count ++;
            }
        }
        if(verification){
            return count == 1;
        }
        return count < 1;
    }

    private boolean fillRemaining(int i, int j){
        if (j>=N && i<N-1)
        {
            i = i + 1;
            j = 0;
        }
        if (i>=N && j>=N)
            return true;

        if (i < SRN)
        {
            if (j < SRN)
                j = SRN;
        }
        else if (i < N-SRN)
        {
            if (j== (i/SRN) *SRN)
                j =  j + SRN;
        }
        else
        {
            if (j == N-SRN)
            {
                i = i + 1;
                j = 0;
                if (i>=N)
                    return true;
            }
        }

        for (int num = 1; num<=N; num++)
        {
            if (CheckIfSafe(i, j, num, false))
            {
                this.starter[i][j] = num;
                if (fillRemaining(i, j+1))
                    return true;

                this.starter[i][j] = 0;
            }
        }
        return false;

    }

    public void removeKDigits(){
        int count = K;
        while(count != 0){
            int cellId = randomGenerator(N*N) - 1;
            int i = (cellId/N);
            int j = cellId % 9;
            if(j != 0){
                j = j - 1;
            }
            if(this.starter[i][j] != 0){
                count --;
                this.starter[i][j] = 0;
            }
        }

    }

    public TableLayout printSudoku(TableLayout tableLayout)
    {
        for(int r = 0; r < N; r ++){
            TableRow row = (TableRow) tableLayout.getChildAt(r);
            for(int c = 0; c < N; c++){
                TextView textView = (TextView) row.getChildAt(c);
                textView.setText(String.valueOf(this.starter[r][c]));
            }

        }

        return tableLayout;

    }

    public void fillValues(){
        this.fillDiagonal();
        this.fillRemaining(0, this.SRN);
        this.removeKDigits();
    }

    public void verify(){

        for(int i = 0; i < this.starter.length; i ++){

            for(int j = 0; j < this.starter.length; j ++){
                boolean result = this.CheckIfSafeVerify(i, j, this.starter[i][j], true);
                if(!result){
                    return;
                }
            }
        }
    }
}
