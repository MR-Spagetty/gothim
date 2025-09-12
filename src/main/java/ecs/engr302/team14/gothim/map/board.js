//Copilot generated suggestion- Not sure what to do with it

const board = [];

for (let x = 0; x < 20; x++) {
  for (let y = 0; y < 20; y++) {
    board.push({
      tileType: "GRASS_Tile",
      position: {
        point: { x, y }
      },
      entities: []
    });
  }
}

console.log(JSON.stringify({ board }, null, 2));