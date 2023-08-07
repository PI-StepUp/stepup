import {FilesetResolver, PoseLandmarker} from "@mediapipe/tasks-vision";
import {DynamicTimeWarping} from "dynamic-time-warping";


var danceCompareNorm:any[] = [];
var danceCompareVec:any[] = [];


const createLandmarker = async () => {
    let poseLandmarker:PoseLandmarker | undefined = undefined;

    const vision = await FilesetResolver.forVisionTasks(
        "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision/wasm"
    );

    poseLandmarker = await PoseLandmarker.createFromOptions(vision, {
        baseOptions: {
          modelAssetPath: `https://storage.googleapis.com/mediapipe-models/pose_landmarker/pose_landmarker_lite/float16/1/pose_landmarker_lite.task`,
        },
        runningMode: "VIDEO",
        numPoses: 1
      });

    console.log(vision);

    return poseLandmarker;
}

// ========== 유사도 점수 반환 ================
async function calculateSimilarity(danceRecord:any[], danceAnswer:any[]) {
  console.log("dance record", danceRecord);

  console.log("dance answer", danceAnswer);

  normalizeDance(danceRecord, danceCompareNorm);
  vectorizeDance(danceCompareNorm, danceCompareVec);

  let similaritySum = 0;
  let frameCount = Math.max(danceAnswer[0][0].length, danceCompareVec[0][0].length);

  
  for (let i = 0; i < 12; i++) {
    let similarity = DTW(danceAnswer[i], danceCompareVec[i]) / frameCount;
    console.log(`similarity : ${similarity}`);
    similaritySum += similarity;
  }
  similaritySum /= 12;

  similaritySum -= 2.2;
  similaritySum *= 30;

  return (100 - Math.floor(similaritySum));
}


// ========== 안무 좌표 정규화(MinMaxScaler) ============

function normalizeDance(dance:any[], danceNorm:any[]) {
  let minX = Number.MAX_VALUE;
  let maxX = Number.MIN_VALUE;
  let minY = Number.MAX_VALUE;
  let maxY = Number.MIN_VALUE;

  for (let frame of dance) {
    for (let bodyPart of frame) {
      if (typeof bodyPart[0] === "number") {
        if (minX > bodyPart[0]) {
          minX = bodyPart[0];
        }

        if (maxX < bodyPart[0]) {
          maxX = bodyPart[0];
        }
      }

      if (typeof bodyPart[1] === "number") {
        if (minY > bodyPart[1]) {
          minY = bodyPart[1];
        }

        if (maxY < bodyPart[1]) {
          maxY = bodyPart[1];
        }
      }
    }
  }

  console.log(`minX : ${minX}, maxX : ${maxX}, minY : ${minY}, maxY : ${maxY}`);
  let normFrame = [];
  for (let frame of dance) {
    for (let bodyPart of frame) {
      if (typeof bodyPart[0] === "number" && typeof bodyPart[1] === "number") {
        normFrame.push([(bodyPart[0] - minX) / (maxX - minX), (bodyPart[1] - minY) / (maxY - minY)]);
      }
    }
    danceNorm.push(normFrame);

    normFrame = [];
  }
}

// =====================================================

// ========== 부위 별 벡터 자료구조 생성 ============

function vectorizeDance(danceNorm:any[], danceVec:any[]) {
  let oneBodyPartVec = [];
  let xVectors = [];
  let yVectors = [];
  let vecTemp:any[] = [];

  for (let i = 0; i < 12; i++) {
    for (let frame of danceNorm) {
      vecTemp = makeBodyPartVector(frame, i);
      if (vecTemp) {
        if ((typeof vecTemp[0] === "number") && (typeof vecTemp[1] === "number")) {
          xVectors.push(vecTemp[0] * 100);
          yVectors.push(vecTemp[1] * 100);
        }
      }
    }
    oneBodyPartVec.push(xVectors);
    oneBodyPartVec.push(yVectors);
    danceVec.push(oneBodyPartVec);

    xVectors = [];
    yVectors = [];
    oneBodyPartVec = [];
  }
}

function makeBodyPartVector(frame:any[], partIndex:number) {
  if (partIndex === 0) {
    if (isNumber(frame[5]) && isNumber(frame[3])) {
      return ([frame[5][0] - frame[3][0], frame[5][1] - frame[3][1]]);
    }
  } else if (partIndex === 1) {
    if (isNumber(frame[3]) && isNumber(frame[1])) {
      return ([frame[3][0] - frame[1][0], frame[3][1] - frame[1][1]]);
    }
  } else if (partIndex === 2) {
    if (isNumber(frame[1]) && isNumber(frame[0])) {
      return ([frame[1][0] - frame[0][0], frame[1][1] - frame[0][1]]);
    }
  } else if (partIndex === 3) {
    if (isNumber(frame[2]) && isNumber(frame[0])) {
      return ([frame[2][0] - frame[0][0], frame[2][1] - frame[0][1]]);
    }
  } else if (partIndex === 4) {
    if (isNumber(frame[4]) && isNumber(frame[2])) {
    return ([frame[4][0] - frame[2][0], frame[4][1] - frame[2][1]]);
    }
  } else if (partIndex === 5) {
    if (isNumber(frame[7]) && isNumber(frame[1])) {
      return ([frame[7][0] - frame[1][0], frame[7][1] - frame[1][1]]);
    }
  } else if (partIndex === 6) {
    if (isNumber(frame[6]) && isNumber(frame[0])) {
      return ([frame[6][0] - frame[0][0], frame[6][1] - frame[0][1]]);
    }
  } else if (partIndex === 7) {
    if (isNumber(frame[7]) && isNumber(frame[6])) {
      return ([frame[7][0] - frame[6][0], frame[7][1] - frame[6][1]]);
    }
  } else if (partIndex === 8) {
    if (isNumber(frame[9]) && isNumber(frame[7])) {
      return ([frame[9][0] - frame[7][0], frame[9][1] - frame[7][1]]);
    }
  } else if (partIndex === 9) {
    if (isNumber(frame[8]) && isNumber(frame[6])) {
      return ([frame[8][0] - frame[6][0], frame[8][1] - frame[6][1]]);
    }
  } else if (partIndex === 10) {
    if (isNumber(frame[11]) && isNumber(frame[9])) {
      return ([frame[11][0] - frame[9][0], frame[11][1] - frame[9][1]]);
    }
  } else if (partIndex === 11) {
    if (isNumber(frame[10]) && isNumber(frame[8])) {
      return ([frame[10][0] - frame[8][0], frame[10][1] - frame[8][1]]);
    }
  }

  return null;
}

function isNumber(frame:any[]) {
  if (typeof frame !== "undefined") {
    return (typeof frame[0] === "number" && typeof frame[1] === "number");
  }
  return false;
}

// ================================================

// calculate DTW
// orlm : origin landmarks
// lm : landmarks to compare
function DTW(orlm:any[], lm:any[]){

  let distFunc = function(a, b) {
      return Math.abs(a - b);
  }
  
  let dtwX = new DynamicTimeWarping(orlm[0], lm[0], distFunc);
  var dtwY = new DynamicTimeWarping(orlm[1], lm[1], distFunc);
  var distX = dtwX.getDistance();
  var distY = dtwY.getDistance();

  return (distX + distY) / 2;
}


export {createLandmarker, calculateSimilarity}