import {FilesetResolver, PoseLandmarker} from "@mediapipe/tasks-vision";

var danceCompareNorm: any[] = [];
var danceCompareVec: any[] = [];


const createLandmarker = async () => {
    let poseLandmarker: PoseLandmarker | undefined = undefined;

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
async function calculateSimilarity(danceRecord: any[], danceAnswer: any[]) {
    console.log("dance record", danceRecord);
    console.log("dance answer", danceAnswer);


    danceCompareNorm = [];
    danceCompareVec = [];

    normalizeDance(danceRecord, danceCompareNorm);
    vectorizeDance(danceCompareNorm, danceCompareVec);

    console.log("normalized", danceCompareNorm);
    console.log("vectorized", danceCompareVec);

    let similaritySumUp = 0;
    let similaritySumDown = 0;
    let frameCount = Math.min(danceAnswer[0][0].length, danceCompareVec[0][0].length);

    danceAnswer = sampleFrames(danceAnswer, frameCount);
    danceCompareVec = sampleFrames(danceCompareVec, frameCount);

    console.log("danceAnswer 샘플링 ", danceAnswer);
    console.log("danceCompareVec 샘플링 ", danceCompareVec);

    //상체
    for (let i = 0; i < 8; i++) {
        let similarity = DTW(danceAnswer[i], danceCompareVec[i]) / frameCount;
        console.log(`similarity : ${similarity}`);
        similaritySumUp += similarity;
    }
    similaritySumUp /= 8;


    //하체
    for (let i = 8; i < 12; i++) {
        let similarity = DTW(danceAnswer[i], danceCompareVec[i]) / frameCount;
        console.log(`similarity : ${similarity}`);
        similaritySumDown += similarity;
    }
    similaritySumDown /= 4;


    console.log("similarity 더한거 ", (similaritySumUp + similaritySumDown)/2);
    console.log("similaritySumUp ", similaritySumUp);
    console.log("similaritySumDown ", similaritySumDown);

    return (100 - Math.floor(transformScore(similaritySumUp*160+similaritySumDown*40)));
}


function sampleFrames(frames: any[], sampleCount: number): any[] {
    const sampledFrames = [];

    for (let i = 0; i < frames.length; i++) {
        const frame = frames[i];
        const sampledFrameSet = [];

        for (let j = 0; j < frame.length; j++) {
            const sampleData = frame[j];
            const sample = [];

            for (let k = 0; k < sampleCount; k++) {
                const index = Math.floor((k / sampleCount) * sampleData.length);
                sample.push(sampleData[index]);
            }

            sampledFrameSet.push(sample);
        }

        sampledFrames.push(sampledFrameSet);
    }

    return sampledFrames;
}


function transformScore(score: number) {
    if (score >= 100) {
        return 99;
    } else {
        return score;
    }
}


// ========== 안무 좌표 정규화(MinMaxScaler) ============
function normalizeDance(dance: any[], danceNorm: any[]) {
    let minX = Number.MAX_VALUE;
    let maxX = Number.MIN_VALUE;
    let minY = Number.MAX_VALUE;
    let maxY = Number.MIN_VALUE;
    let minZ = Number.MAX_VALUE;
    let maxZ = Number.MIN_VALUE;

    for (let frame of dance) {
        for (let bodyPart of frame) {
            if (bodyPart) {
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

                if (typeof bodyPart[2] === "number") {
                    if (minZ > bodyPart[2]) {
                        minZ = bodyPart[2];
                    }

                    if (maxZ < bodyPart[2]) {
                        maxZ = bodyPart[2];
                    }
                }
            }
        }
    }

    console.log(`minX : ${minX}, maxX : ${maxX}, minY : ${minY}, maxY : ${maxY}, minZ : ${minZ}, maxZ : ${maxZ}`);

    let normFrame = [];
    for (let frame of dance) {
        for (let bodyPart of frame) {
            if (bodyPart) {
                if (typeof bodyPart[0] === "number" && typeof bodyPart[1] === "number" && typeof bodyPart[2] === "number") {
                    // 3차원 좌표의 정규화 로직
                    normFrame.push([
                        (bodyPart[0] - minX) / (maxX - minX),
                        (bodyPart[1] - minY) / (maxY - minY),
                        (bodyPart[2] - minZ) / (maxZ - minZ)
                    ]);
                }
            }
        }
        danceNorm.push(normFrame);
        normFrame = [];
    }
}

// ========== 부위 별 벡터 자료구조 생성 ============
function vectorizeDance(danceNorm: any[], danceVec: any[]) {
    for (let i = 0; i < 12; i++) {
        let oneBodyPartVec = [];
        let xVectors = [];
        let yVectors = [];
        let zVectors = [];

        let vecTemp: any[] = [];

        for (let frame of danceNorm) {
            vecTemp = makeBodyPartVector(frame, i)!;
            // 3차원 좌표의 벡터화 로직
            if (vecTemp) {
                if ((typeof vecTemp[0] === "number") &&
                    (typeof vecTemp[1] === "number") &&
                    (typeof vecTemp[2] === "number")) {
                    xVectors.push(Math.round(vecTemp[0] * 10000) / 100);
                    yVectors.push(Math.round(vecTemp[1] * 10000) / 100);
                    zVectors.push(Math.round(vecTemp[2] * 10000) / 100);
                }
            }
        }

        oneBodyPartVec.push(xVectors);
        oneBodyPartVec.push(yVectors);
        oneBodyPartVec.push(zVectors);
        danceVec.push(oneBodyPartVec);

        xVectors = [];
        yVectors = [];
        zVectors = [];
        oneBodyPartVec = [];
    }
}

function makeBodyPartVector(frame: any[], partIndex: number) {
    if (partIndex === 0) {
        if (isNumber(frame[5]) && isNumber(frame[3])) {
            return [
                frame[5][0] - frame[3][0],
                frame[5][1] - frame[3][1],
                frame[5][2] - frame[3][2]
            ];
        }
    } else if (partIndex === 1) {
        if (isNumber(frame[3]) && isNumber(frame[1])) {
            return [
                frame[3][0] - frame[1][0],
                frame[3][1] - frame[1][1],
                frame[3][2] - frame[1][2]
            ];
        }
    } else if (partIndex === 2) {
        if (isNumber(frame[1]) && isNumber(frame[0])) {
            return [
                frame[1][0] - frame[0][0],
                frame[1][1] - frame[0][1],
                frame[1][2] - frame[0][2]
            ];
        }
    } else if (partIndex === 3) {
        if (isNumber(frame[2]) && isNumber(frame[0])) {
            return [
                frame[2][0] - frame[0][0],
                frame[2][1] - frame[0][1],
                frame[2][2] - frame[0][2]
            ];
        }
    } else if (partIndex === 4) {
        if (isNumber(frame[4]) && isNumber(frame[2])) {
            return [
                frame[4][0] - frame[2][0],
                frame[4][1] - frame[2][1],
                frame[4][2] - frame[2][2]
            ];
        }
    } else if (partIndex === 5) {
        if (isNumber(frame[7]) && isNumber(frame[1])) {
            return [
                frame[7][0] - frame[1][0],
                frame[7][1] - frame[1][1],
                frame[7][2] - frame[1][2]
            ];
        }
    } else if (partIndex === 6) {
        if (isNumber(frame[6]) && isNumber(frame[0])) {
            return [
                frame[6][0] - frame[0][0],
                frame[6][1] - frame[0][1],
                frame[6][2] - frame[0][2]
            ];
        }
    } else if (partIndex === 7) {
        if (isNumber(frame[7]) && isNumber(frame[6])) {
            return [
                frame[7][0] - frame[6][0],
                frame[7][1] - frame[6][1],
                frame[7][2] - frame[6][2]
            ];
        }
    } else if (partIndex === 8) {
        if (isNumber(frame[9]) && isNumber(frame[7])) {
            return [
                frame[9][0] - frame[7][0],
                frame[9][1] - frame[7][1],
                frame[9][2] - frame[7][2]
            ];
        }
    } else if (partIndex === 9) {
        if (isNumber(frame[8]) && isNumber(frame[6])) {
            return [
                frame[8][0] - frame[6][0],
                frame[8][1] - frame[6][1],
                frame[8][2] - frame[6][2]
            ];
        }
    } else if (partIndex === 10) {
        if (isNumber(frame[11]) && isNumber(frame[9])) {
            return [
                frame[11][0] - frame[9][0],
                frame[11][1] - frame[9][1],
                frame[11][2] - frame[9][2]
            ];
        }
    } else if (partIndex === 11) {
        if (isNumber(frame[10]) && isNumber(frame[8])) {
            return [
                frame[10][0] - frame[8][0],
                frame[10][1] - frame[8][1],
                frame[10][2] - frame[8][2]
            ];
        }
    }

    return null;
}


// ==========================================

// calculate DTW
// orlm : origin landmarks
// lm : landmarks to compare
function DTW(vec1: any[], vec2: any[]) {
    let distFunc = function (a: any[], b: any[]) {
        var sum = 0;
        for (var i = 0; i < 3; i++) {
            sum += Math.pow(a[i] - b[i], 2);
        }
        return Math.sqrt(sum);
    }

    const n = vec1.length;
    const m = vec2.length;
    const dtwMatrix = Array.from({length: n}, () => Array(m).fill(0));

    dtwMatrix[0][0] = distFunc(vec1[0], vec2[0]);
    for (let i = 1; i < n; i++) {
        dtwMatrix[i][0] = dtwMatrix[i - 1][0] + distFunc(vec1[i], vec2[0]);
    }
    for (let j = 1; j < m; j++) {
        dtwMatrix[0][j] = dtwMatrix[0][j - 1] + distFunc(vec1[0], vec2[j]);
    }

    for (let i = 1; i < n; i++) {
        for (let j = 1; j < m; j++) {
            const cost = distFunc(vec1[i], vec2[j]);
            dtwMatrix[i][j] = cost + Math.min(dtwMatrix[i - 1][j], dtwMatrix[i][j - 1], dtwMatrix[i - 1][j - 1]);
        }
    }

    return dtwMatrix[n - 1][m - 1];
}

function isNumber(frame: any[]) {
    if (typeof frame !== "undefined") {
        return ((typeof frame[0] === "number") &&
            (typeof frame[1] === "number") &&
            (typeof frame[2] === "number"));
    }
    return false;
}


export {createLandmarker, calculateSimilarity}
