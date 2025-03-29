Authors: Tristan Karno and Michael Hargraves
Date: Friday March 28

before code snippet:

public void makeAdjList() {
    for(int i = 0; i < numRows; i++) {
        for(int j = 0; j < numColumns; j++) {
            BoardCell cell = getCell(i, j);
            if (cell.isDoorway()) {
                DoorDirection dir = cell.getDoorDirection();
                if (dir == DoorDirection.UP) {
                    cell.addAdjacency(getRoom(getCell(i-1, j)).getCenterCell());
                } else if (dir == DoorDirection.DOWN) {
                    cell.addAdjacency(getRoom(getCell(i+1, j)).getCenterCell());
                } else if (dir == DoorDirection.RIGHT) {
                    cell.addAdjacency(getRoom(getCell(i, j+1)).getCenterCell());
                } else if (dir == DoorDirection.LEFT) {
                    cell.addAdjacency(getRoom(getCell(i, j-1)).getCenterCell());
                }
            }

            if (!cell.isRoom()) {
                if (i > 0) {
                    BoardCell up = getCell(i - 1, j);
                    if (up.getInitial() == WALKWAY_INITIAL || up.isDoorway()) {
                        cell.addAdjacency(up);
                    }
                }
                if (j > 0) {
                    BoardCell left = getCell(i, j - 1);
                    if (left.getInitial() == WALKWAY_INITIAL || left.isDoorway()) {
                        cell.addAdjacency(left);
                    }
                }
                if (i < numRows - 1) {
                    BoardCell down = getCell(i + 1, j);
                    if (down.getInitial() == WALKWAY_INITIAL || down.isDoorway()) {
                        cell.addAdjacency(down);
                    }
                }
                if (j < numColumns - 1) {
                    BoardCell right = getCell(i, j + 1);
                    if (right.getInitial() == WALKWAY_INITIAL || right.isDoorway()) {
                        cell.addAdjacency(right);
                    }
                }
            } else {
                Room room = getRoom(cell);
                for (BoardCell door : room.getDoorCell()) {
                    cell.addAdjacency(door);
                }
                if (cell.getSecretPassage() != NO_SECRET_PASSAGE) {
                    BoardCell target = getRoom(cell.getSecretPassage()).getCenterCell();
                    cell.addAdjacency(target);
                }
            }
        }
    }
}


after code snippet:

public void makeAdjList() {
    for (int i = 0; i < numRows; i++) {
        for (int j = 0; j < numColumns; j++) {
            BoardCell cell = getCell(i, j);

            if (cell.isDoorway()) {
                handleDoorwayAdjacency(cell, i, j);
            }

            if (!cell.isRoom()) {
                handleWalkwayAdjacency(cell, i, j);
            } else {
                handleRoomAdjacency(cell);
            }
        }
    }
}

private void handleDoorwayAdjacency(BoardCell cell, int row, int col) {
    DoorDirection dir = cell.getDoorDirection();
    switch (dir) {
        case UP -> cell.addAdjacency(getRoom(getCell(row - 1, col)).getCenterCell());
        case DOWN -> cell.addAdjacency(getRoom(getCell(row + 1, col)).getCenterCell());
        case LEFT -> cell.addAdjacency(getRoom(getCell(row, col - 1)).getCenterCell());
        case RIGHT -> cell.addAdjacency(getRoom(getCell(row, col + 1)).getCenterCell());
    }
}

private void handleWalkwayAdjacency(BoardCell cell, int row, int col) {
    if (row > 0) {
        BoardCell up = getCell(row - 1, col);
        if (up.getInitial() == WALKWAY_INITIAL || up.isDoorway()) cell.addAdjacency(up);
    }
    if (col > 0) {
        BoardCell left = getCell(row, col - 1);
        if (left.getInitial() == WALKWAY_INITIAL || left.isDoorway()) cell.addAdjacency(left);
    }
    if (row < numRows - 1) {
        BoardCell down = getCell(row + 1, col);
        if (down.getInitial() == WALKWAY_INITIAL || down.isDoorway()) cell.addAdjacency(down);
    }
    if (col < numColumns - 1) {
        BoardCell right = getCell(row, col + 1);
        if (right.getInitial() == WALKWAY_INITIAL || right.isDoorway()) cell.addAdjacency(right);
    }
}

private void handleRoomAdjacency(BoardCell cell) {
    Room room = getRoom(cell);
    for (BoardCell door : room.getDoorCell()) {
        cell.addAdjacency(door);
    }

    if (cell.hasSecretPassage()) {
        BoardCell target = getRoom(cell.getSecretPassage()).getCenterCell();
        cell.addAdjacency(target);
    }
}
