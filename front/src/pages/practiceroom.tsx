import React, {useState, useEffect, useCallback, useRef} from "react";

import LeftArrowIcon from "/public/images/icon-left-arrow.svg"
import ReflectIcon from "/public/images/icon-reflect.svg"
import PlayThumbnail from "/public/images/room-playlist-thumbnail.jpg"
import PlayIcon from "/public/images/icon-play.svg"
import ReflectHoverIcon from "/public/images/icon-hover-reflect.svg"

import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";

import Image from "next/image"
import Link from "next/link"

import { axiosMusic, axiosUser } from "apis/axios";
import { accessTokenState, refreshTokenState, idState } from "states/states";

import { createLandmarker, calculateSimilarity } from "../utils/motionsetter";
import { PoseLandmarker, DrawingUtils } from "@mediapipe/tasks-vision";
import axios from "axios";
import { useRouter } from "next/router";

const EMBED_URL: any = {
    1: "https://www.youtube.com/embed/g4vaGXR7fUY",
    2: "https://www.youtube.com/embed/VyQ40mNYx3Q",
    3: "https://www.youtube.com/embed/2U7QXp9ItqM",
    4: "https://www.youtube.com/embed/WhPr4rC-bAU",
    5: "https://www.youtube.com/embed/bGa1g4jg-MA",
    6: "https://www.youtube.com/embed/klNlrKzcQbc",
    7: "https://www.youtube.com/embed/quT7eRenhxw",
    8: "https://www.youtube.com/embed/Y063oeFDIGA",
    9: "https://www.youtube.com/embed/w-TfkfN6vrw",
    10: "https://www.youtube.com/embed/V9SmaLFFPqM",
    11: "https://www.youtube.com/embed/PYWxkQzp1oY",
    12: "https://www.youtube.com/embed/3we9E99GK2A",
    13: "https://www.youtube.com/embed/rHwdB-J49Ks",
    14: "https://www.youtube.com/embed/mB0tL-7M6VQ",
    15: "https://www.youtube.com/embed/yGJ-1LkaRho",
    16: "https://www.youtube.com/embed/HZb7CWKUaOw",
    17: "https://www.youtube.com/embed/39Y8PkJRZTw",
    18: "https://www.youtube.com/embed/9pIGXdUgCmE",
    19: "https://www.youtube.com/embed/r4jiLONU8R8",
    20: "https://www.youtube.com/embed/n4I0dwD6u1k",
    21: "https://www.youtube.com/embed/I_GEa-Dud6A",
    22: "https://www.youtube.com/embed/4cxU3TJV-CQ",
    23: "https://www.youtube.com/embed/FUxKgi_BDVI",
    24: "https://www.youtube.com/embed/wWJU-nYV-no",
    25: "https://www.youtube.com/embed/QVyaGJRsaVI",
    26: "https://www.youtube.com/embed/CMCKkVbzGfU",
    27: "https://www.youtube.com/embed/FbY8w-eVuz0",
    28: "https://www.youtube.com/embed/VuEh-4UKfqs",
    29: "https://www.youtube.com/embed/B9mgikpIl98",
    30: "https://www.youtube.com/embed/RcojjcsBkUo",
    31: "https://www.youtube.com/embed/yIX33lK7vpo",
    32: "https://www.youtube.com/embed/TtRBMl-K9Xs",
    33: "https://www.youtube.com/embed/pgCyvRoDpB0",
    34: "https://www.youtube.com/embed/1E5Q1AdAxMg",
    35: "https://www.youtube.com/embed/JteGnlZC8K4",
    36: "https://www.youtube.com/embed/RdjCtKJWNVY",
    37: "https://www.youtube.com/embed/yln8wDZ-i4E",
    38: "https://www.youtube.com/embed/96gMuaVE-Bo",
    39: "https://www.youtube.com/embed/pxNSGBU82GY",
    40: "https://www.youtube.com/embed/XknszxBeP7Y",
}

let poseLandmarker: PoseLandmarker;
let drawingUtils: DrawingUtils;
let canvasCtx: any;
let saveMotion = false;
let danceRecord: any[] = [];

const PracticeRoom = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
    const [reflect, setReflect] = useState(false);
    const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
    const [id, setId] = useRecoilState(idState);
    const [musics, setMusics] = useState<any>();
    const [count3, setCount3] = useState(false);
    const [count2, setCount2] = useState(false);
    const [count1, setCount1] = useState(false);
    const [playResult, setPlayResult] = useState('');
    const [resultScore, setResultScore] = useState<number>();
    const [selectedMusic, setSelectedMusic] = useState<any>(14);
    const [scoreCount, setScoreCount] = useState<any>(0);
    const localVideoRef = useRef<any>(null);
    const localCanvasRef = useRef<HTMLCanvasElement>(null);
	const localStreamRef = useRef<MediaStream>();
    const myVideoDivRef = useRef<HTMLDivElement>(null);
    const router = useRouter();
    const hostToken = router.query.token;

    const reflectHover = () => {
        setReflect(true);
    }
    const reflectLeave = () => {
        setReflect(false);
    }

    const getLocalStream = useCallback(async () => {
		try {
			const localStream = await navigator.mediaDevices.getUserMedia({
				audio: true,
				video: {
					width: 240,
					height: 240,
				},
			});
			localStreamRef.current = localStream;
			if (localVideoRef.current){
                localVideoRef.current.srcObject = localStream;
                localVideoRef.current.play();
                console.log("video 실행");
                localVideoRef.current.addEventListener("loadeddata", makePoseLandmarker);
            }
		} catch (e) {
			console.log(`getUserMedia error: ${e}`);
		}
	}, []);

    useEffect(() => {
        getLocalStream();

        axios.get("https://stepup-pi.com:8080/api/music",{
            params:{
                keyword: "",
            },
            headers:{
                Authorization: `Bearer ${accessToken}`,
            }
        }).then((data) => {
            console.log(data);
            if(data.data.message === "노래 목록 조회 완료"){
                setMusics(data.data.data);
            }
        })
    }, [])

    const changeMusic = (musicId: any) => {
        setSelectedMusic(musicId);
    }

    // ======= PoseLandmarker 생성 =======
	async function makePoseLandmarker() {
        canvasCtx = localCanvasRef.current!.getContext("2d");
        drawingUtils = new DrawingUtils(canvasCtx);
        console.log("drawingutils 생성?", drawingUtils);
        await createLandmarker().then((landMarker) => {
            poseLandmarker = landMarker;
            console.log("poselandmarker 생성", poseLandmarker);
        });
    }

    async function startMeasure() {
        await setCount3(true);
        await setTimeout(async() => {
            await setCount3(false);
            await setCount2(true);
            setTimeout(async () => {
                await setCount2(false);
                await setCount1(true);
                setTimeout(async () => {
                    await setCount1(false);
                    
                    await startPredictAndCalcSimilarity(selectedMusic);
                }, 2000);
            }, 2000);
        }, 2000);
    }


	let lastVideoTime: number | undefined = -1;
    
    async function startPredictAndCalcSimilarity(musicId:number) {
        saveMotion = true;
        localCanvasRef.current!.style.display = "block";
        danceRecord = [];
        
        const response = await getAnswerData(musicId);
        console.log("response 값", response);
        
        predictWebcam();

        return setTimeout(async () => {
            saveMotion = false;
            localCanvasRef.current!.style.display = "none";
            console.log("측정 종료");
            console.log("측정 기록", danceRecord);

            const danceAnswer = await JSON.parse(response.data.answer);
            console.log("danceAnswer 값", response.data);

            const score = await calculateSimilarity(danceRecord, danceAnswer);
            console.log(score);
            setResultScore(score);

            if(score < 60){
                setPlayResult("failure");
                setTimeout(() => {
                    setPlayResult("");
                }, 5000)
            }else{
                setPlayResult("success");
                setTimeout(() => {
                    setPlayResult("");
                }, 5000)
            }

            return score;
        }, (response.data.playtime + 2)*1000);
    }

    let frameCount = 0;
	// ============ 모션 인식 =============
    async function predictWebcam() {
		if (!poseLandmarker) {
            console.log("pose landmarker not loaded!");
            await makePoseLandmarker();
            predictWebcam();
			return;
		}

		let startTimeMs = await performance.now();
        frameCount = 0;


		if (lastVideoTime !== localVideoRef.current?.currentTime) {
			lastVideoTime = localVideoRef.current?.currentTime;

			await poseLandmarker.detectForVideo(localVideoRef.current, startTimeMs, (result) => {
                console.log(result);
                frameCount += 1;
                console.log("framecount", frameCount);
				setDance(result, danceRecord);

                canvasCtx.save();
                canvasCtx.clearRect(0, 0, localCanvasRef.current!.width, localCanvasRef.current!.height);
                for (const landmark of result.landmarks) {
                    drawingUtils.drawLandmarks(landmark, {
                        radius: (data) => DrawingUtils.lerp(data.from!.z, -0.15, 0.1, 5, 1)
                    });
                    drawingUtils.drawConnectors(landmark, PoseLandmarker.POSE_CONNECTIONS);
                }
                canvasCtx.restore();
			});
		}

        if (saveMotion) {
			window.requestAnimationFrame(predictWebcam);
        }
	}

	// ========== 안무 좌표 저장 ============

	async function setDance(result: any, dance: any[]) {
		let coordinate;
        let oneFrame = [];

        for(let i = 11; i < 29; i++){
        if (17 <= i && i <= 22) continue;
        
        if (typeof result.landmarks[0] !== "undefined") {
            coordinate = [result.landmarks[0][i].x, result.landmarks[0][i].y, result.landmarks[0][i].z];
        }
            oneFrame.push(coordinate); 
        }

        dance.push(oneFrame);
	}

	// =====================================

	// ==============정답 데이터 get=====================

    async function getAnswerData(musicId:number) {
        try {
            const response = await axios.get(`https://stepup-pi.com:8080/api/music/${musicId}`, {
                params:{
                    musicId: musicId,
                },
                headers: {
                    Authorization: `Bearer ${hostToken}`, 
                },
            });
            const responseData = await response.data;
            console.log("successfully!");
            console.log("responseData 값", responseData);
            return await responseData;
        } catch (error) {
            console.error("Error:", error);
        }
        
    }

    function easeOutCirc(x: number): number {
        return Math.sqrt(1 - Math.pow(x - 1, 2));
    }

    function useCountNum(end: number, start = 0, duration = 2000) {
        const frameRate = 1000 / 60
        const totalFrame = Math.round(duration / frameRate)
        setScoreCount(0);
      
        useEffect(() => {
          let currentNumber = start
          const counter = setInterval(() => {
            const progress = easeOutCirc(++currentNumber / totalFrame)
            setScoreCount(Math.round(end * progress))
      
            if (progress === 1) {
              clearInterval(counter)
            }
          }, frameRate)
        }, [end, frameRate, start, totalFrame])
      
        return scoreCount;
    }

    async function reflectMyVideo() {
        console.log("!reflect", reflect);

        if (!reflect) {
            myVideoDivRef.current?.setAttribute("class", "my-video reflect-video");
            setReflect(true);
        } else {
            myVideoDivRef.current?.setAttribute("class", "my-video");
            setReflect(false);
        }
    }

    const stopMediaTracks = (stream: any) => {
        if (!stream) return;
        stream.getTracks().forEach((track: any) => {
          track.stop();
        });
    };

    useEffect(() => {
        return () => {
            if (localStreamRef.current) {
                const videoTrack = localStreamRef.current.getVideoTracks()[0];
                if (videoTrack) {
                    videoTrack.stop();
                }
            }

            stopMediaTracks(localStreamRef.current);

            localVideoRef.current = null;
        }
    }, [])

    return(
        <>
            <div className="practiceroom-wrap">
                {/* <SideMenu/> */}
                <div className="remove-sidemenu practice-video-wrap">
                    <div className="practice-title">
                        <div className="pre-icon">
                            <Link href="/"><Image src={LeftArrowIcon} alt=""/></Link>
                        </div>
                        <div className="room-title">
                            <h3>Step Up 연습실</h3>
                            <span>K-Pop 댄스에 도전해 높은 점수를 노려보세요!</span>
                        </div>
                    </div>

                    <div className="video-content">
                        <div className="my-video youtube-video">
                        {
                            saveMotion ?
                            <iframe className="make-border" src={`${EMBED_URL[selectedMusic]}?autoplay=1`} allow="autoplay"></iframe>
                            :
                            <iframe className="make-border" src={`${EMBED_URL[selectedMusic]}`} allow="autoplay"></iframe>
                        }                            
                        </div>

                        <div ref={myVideoDivRef} className="my-video reflect-video" style={{ position: "relative", top: "0px", left: "0px" }}>
                            <video className="make-border" src="" playsInline ref={localVideoRef}></video>
                            <canvas ref={localCanvasRef} style={{position:"absolute", left: "0px", top: "0px", width: "100%", height:"100%"}}></canvas>
                        </div>
                        
                        <div className="control-wrap">
                            <ul>
                                <li onMouseEnter = {reflectHover} onMouseLeave = {reflectLeave}>
                                    <button onClick={reflectMyVideo}>
                                        {reflect ? <Image src={ReflectHoverIcon} alt=""/> : <Image src={ReflectIcon} alt=""/>}
                                    </button>
                                </li>
                                <li><button onClick={startMeasure} className="exit-button">{lang==="en" ? "Perfect Score" : lang==="cn" ? "满分" : "퍼펙트 스코어" }</button></li>
                            </ul>
                        </div>
                    </div>
                    
                </div>
                <div className="musiclist">
                    <div className="musiclist-title">
                        <h3>{lang==="en" ? "List of practice rooms" : lang==="cn" ? "练习室列表" : "연습실 목록" }</h3>
                        <span>{musics?.length}</span>
                    </div>
                    <div className="musiclist-content">
                        <ul>
                            {musics?.map((music:any, index: any) => {
                                return(
                                    <li key={index} onClick={() => changeMusic(music.musicId)}>
                                        <div className="flex-wrap">
                                            <div className="musiclist-content-thumbnail">
                                                {
                                                    music.url ?
                                                    <Image src={music.url} alt="" width={40} height={40}/>
                                                    :
                                                    <Image src={PlayThumbnail} alt=""/>
                                                }
                                                
                                            </div>
                                            <div className="musiclist-content-info">
                                                <h4>{music.title}</h4>
                                                <span>{music.artist}</span>
                                            </div>
                                        </div>
                                        <div className="musiclist-content-control-icon">
                                            <span><Image src={PlayIcon} alt=""/></span>
                                        </div>
                                    </li>
                                )
                            })}
                        </ul>
                    </div>
                </div>
            </div>
            {
                count3 ?
                <div className="count-3" style={{animationName: "big-animation"}}>3</div>
                :
                <></>
            }
            {
                count2 ?
                <div className="count-2" style={{animationName: "big-animation"}}>2</div>
                :
                <></>
            }
            {
                count1 ?
                <div className="count-1" style={{animationName: "big-animation"}}>1</div>
                :
                <></>
            }
            {
                playResult === "success" ?
                <>
                    <div className="confetti">
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                    </div>
                    <div className="result-logo">
                        <svg width="450" height="78" viewBox="0 0 450 78" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M28.7 77.4C19 77.4 8.6 73.8 0.9 66.7L11.1 54.4C16.3 58.9 23.2 62 29.1 62C35.7 62 38.8 59.5 38.8 55.5C38.8 51.2 34.7 49.8 28.3 47.1L18.8 43.1C11 40 3.7 33.5 3.7 22.7C3.7 10.2 14.9 0.199998 30.7 0.199998C39.3 0.199998 48.4 3.5 54.9 10L46 21.2C41.1 17.5 36.5 15.5 30.7 15.5C25.2 15.5 21.9 17.7 21.9 21.6C21.9 25.8 26.5 27.4 33.1 30L42.4 33.7C51.6 37.4 57.1 43.6 57.1 54.1C57.1 66.5 46.7 77.4 28.7 77.4ZM99.2672 77.4C79.3672 77.4 69.0672 66.1 69.0672 41V1.49999H86.8672V42.9C86.8672 57 91.0672 62 99.2672 62C107.367 62 111.867 57 111.867 42.9V1.49999H129.067V41C129.067 66.1 119.067 77.4 99.2672 77.4ZM177.237 77.4C158.337 77.4 142.337 64.4 142.337 39.2C142.337 14.4 158.937 0.199998 177.837 0.199998C187.537 0.199998 195.337 4.69999 200.437 9.9L191.137 21.3C187.337 17.9 183.437 15.5 178.137 15.5C168.537 15.5 160.637 24.1 160.637 38.6C160.637 53.4 167.437 62 177.937 62C183.937 62 188.637 59 192.137 55.2L201.537 66.4C195.237 73.7 186.937 77.4 177.237 77.4ZM238.858 77.4C219.958 77.4 203.958 64.4 203.958 39.2C203.958 14.4 220.558 0.199998 239.458 0.199998C249.158 0.199998 256.958 4.69999 262.058 9.9L252.758 21.3C248.958 17.9 245.058 15.5 239.758 15.5C230.158 15.5 222.258 24.1 222.258 38.6C222.258 53.4 229.058 62 239.558 62C245.558 62 250.258 59 253.758 55.2L263.158 66.4C256.858 73.7 248.558 77.4 238.858 77.4ZM274.057 76V1.49999H321.257V16.4H291.957V30.2H316.857V45.2H291.957V61H322.357V76H274.057ZM360.145 77.4C350.445 77.4 340.045 73.8 332.345 66.7L342.545 54.4C347.745 58.9 354.645 62 360.545 62C367.145 62 370.245 59.5 370.245 55.5C370.245 51.2 366.145 49.8 359.745 47.1L350.245 43.1C342.445 40 335.145 33.5 335.145 22.7C335.145 10.2 346.345 0.199998 362.145 0.199998C370.745 0.199998 379.845 3.5 386.345 10L377.445 21.2C372.545 17.5 367.945 15.5 362.145 15.5C356.645 15.5 353.345 17.7 353.345 21.6C353.345 25.8 357.945 27.4 364.545 30L373.845 33.7C383.045 37.4 388.545 43.6 388.545 54.1C388.545 66.5 378.145 77.4 360.145 77.4ZM421.18 77.4C411.48 77.4 401.08 73.8 393.38 66.7L403.58 54.4C408.78 58.9 415.68 62 421.58 62C428.18 62 431.28 59.5 431.28 55.5C431.28 51.2 427.18 49.8 420.78 47.1L411.28 43.1C403.48 40 396.18 33.5 396.18 22.7C396.18 10.2 407.38 0.199998 423.18 0.199998C431.78 0.199998 440.88 3.5 447.38 10L438.48 21.2C433.58 17.5 428.98 15.5 423.18 15.5C417.68 15.5 414.38 17.7 414.38 21.6C414.38 25.8 418.98 27.4 425.58 30L434.88 33.7C444.08 37.4 449.58 43.6 449.58 54.1C449.58 66.5 439.18 77.4 421.18 77.4Z" fill="url(#paint0_linear_458_11)"/>
                            <defs>
                            <linearGradient id="paint0_linear_458_11" x1="-3" y1="33" x2="454" y2="33" gradientUnits="userSpaceOnUse">
                            <stop stop-color="#0D51F2"/>
                            <stop offset="1" stop-color="#0DABF2"/>
                            </linearGradient>
                            </defs>
                        </svg>
                    </div>
                </>
                :
                playResult === "failure" ?
                <div className="result-logo">
                    <svg width="407" height="77" viewBox="0 0 407 77" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M0.6 75V0.499994H48.1V15.4H18.5V31.7H43.9V46.6H18.5V75H0.6ZM46.3758 75L69.4758 0.499994H90.8758L113.976 75H95.1758L85.8758 37.8C83.8758 30.4 81.9758 21.5 79.9758 13.9H79.5758C77.7758 21.7 75.7758 30.4 73.8758 37.8L64.5758 75H46.3758ZM62.0758 57.9V44.1H98.0758V57.9H62.0758ZM121.791 75V0.499994H139.691V75H121.791ZM156.752 75V0.499994H174.652V60H203.652V75H156.752ZM241.24 76.4C221.34 76.4 211.04 65.1 211.04 40V0.499994H228.84V41.9C228.84 56 233.04 61 241.24 61C249.34 61 253.84 56 253.84 41.9V0.499994H271.04V40C271.04 65.1 261.04 76.4 241.24 76.4ZM287.709 75V0.499994H315.009C330.909 0.499994 344.109 5.99999 344.109 24C344.109 41.5 330.909 48.8 315.009 48.8H305.609V75H287.709ZM305.609 34.7H313.609C322.109 34.7 326.709 31 326.709 24C326.709 17 322.109 14.7 313.609 14.7H305.609V34.7ZM327.909 75L311.809 44.2L323.909 32.5L347.909 75H327.909ZM358.51 75V0.499994H405.71V15.4H376.41V29.2H401.31V44.2H376.41V60H406.81V75H358.51Z" fill="url(#paint0_linear_459_10)"/>
                        <defs>
                        <linearGradient id="paint0_linear_459_10" x1="-8" y1="32" x2="427.5" y2="32" gradientUnits="userSpaceOnUse">
                        <stop stop-color="#0D52F2"/>
                        <stop offset="1" stop-color="#0DAAF2"/>
                        </linearGradient>
                        </defs>
                    </svg>
                </div>
                :
                <></>
            }
        </>
    )
}

export default PracticeRoom;