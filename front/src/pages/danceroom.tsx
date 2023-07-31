import React, {useState, useRef, useEffect, useCallback} from "react";
import io from 'socket.io-client';

import SideMenu from "components/SideMenu";
import Video from "components/Video";

import Image from "next/image";
import LeftArrowIcon from "/public/images/icon-left-arrow.svg"
import GroupIcon from "/public/images/icon-group.svg"
import ReflectIcon from "/public/images/icon-reflect.svg"
import CameraIcon from "/public/images/icon-camera.svg"
import MicIcon from "/public/images/icon-mic.svg"
import MoreIcon from "/public/images/icon-more-dot.svg"
import ChatDefaultImg from "/public/images/chat-default-profile-img.svg"
import sendImg from "/public/images/send-img.svg"
import ReflectHoverIcon from "/public/images/icon-hover-reflect.svg"
import MicHoverIcon from "/public/images/icon-hover-mic.svg"
import CameraHoverIcon from "/public/images/icon-hover-camera.svg"
import MoreDotHoverIcon from "/public/images/icon-hover-more-dot.svg"

import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";

const pc_config = {
	iceServers: [
		// {
		//   urls: 'stun:[STUN_IP]:[PORT]',
		//   'credentials': '[YOR CREDENTIALS]',
		//   'username': '[USERNAME]'
		// },
		{
			urls: 'stun:stun.l.google.com:19302',
		},
	],
};
const SOCKET_SERVER_URL = 'localhost:4002';

const DanceRoom = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
    const socketRef = useRef<SocketIOClient.Socket>();
    const localStreamRef = useRef<MediaStream>();
    const sendPCRef = useRef<RTCPeerConnection>();
    const receivePCsRef = useRef<{ [socketId: string]: RTCPeerConnection }>({});
    const [users, setUsers] = useState<Array<WebRTCUser>>([]);

    const localVideoRef = useRef<HTMLVideoElement>(null);
    const [msgList, setMsgList] = useState<any[]>([]);
    const inputChat = useRef(null);
    const chatContent = useRef(null);
    const roomName = 1;

    // 컨트롤러 hover 시 변경
    const [reflect, setReflect] = useState(false);
    const [mic, setMic] = useState(false);
    const [camera, setCamera] = useState(false);
    const [moredot, setMoredot] = useState(false);
    const reflectHover = () => {
        setReflect(true);
    }
    const reflectLeave = () => {
        setReflect(false);
    }
    const micHover = () => {
        setMic(true);
    }
    const micLeave = () => {
        setMic(false);
    }
    const cameraHover = () => {
        setCamera(true);
    }
    const cameraLeave = () => {
        setCamera(false);
    }
    const moreDotHover = () => {
        setMoredot(true);
    }
    const moreDotLeave = () => {
        setMoredot(false);
    }

    const sendMessage = () => {
        socketRef.current.emit("send_message", inputChat.current?.value, roomName);
        socketRef.current.on("message", (message: any) => {
            console.log("실행되나요?");
            setMsgList(msgList => [...msgList, message]);
        });
        if(inputChat.current != null){
            inputChat.current.value = "";
        }
        scrollToBottom();
    }

    const scrollToBottom = () => {
        if(chatContent.current != null){
            chatContent.current.scrollTop = chatContent.current.scrollHeight;
        }
    }

    const handleKeyPress = (e: KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
            socketRef.current.emit("send_message", inputChat.current?.value, roomName);
            if(inputChat.current != null){
                inputChat.current.value = "";
            }
            scrollToBottom();
        }
    };



    const closeReceivePC = useCallback((id: string) => {
        if (!receivePCsRef.current[id]) return;
        receivePCsRef.current[id].close();
        delete receivePCsRef.current[id];
    }, []);

    const createReceiverOffer = useCallback(
        async (pc: RTCPeerConnection, senderSocketID: string) => {
        try {
            const sdp = await pc.createOffer({
            offerToReceiveAudio: true,
            offerToReceiveVideo: true,
            });
            console.log("create receiver offer success");
            await pc.setLocalDescription(new RTCSessionDescription(sdp));

            if (!socketRef.current) return;
            socketRef.current.emit("receiverOffer", {
                sdp,
                receiverSocketID: socketRef.current.id,
                senderSocketID,
                roomID: roomName,
            });
        } catch (error) {
            console.log(error);
        }
        },
        []
    );

    const createReceiverPeerConnection = useCallback((socketID: string) => {
        try {
        const pc = new RTCPeerConnection(pc_config);

        // add pc to peerConnections object
        receivePCsRef.current = { ...receivePCsRef.current, [socketID]: pc };

        pc.onicecandidate = (e) => {
            if (!(e.candidate && socketRef.current)) return;
            console.log("receiver PC onicecandidate");
            socketRef.current.emit("receiverCandidate", {
            candidate: e.candidate,
            receiverSocketID: socketRef.current.id,
            senderSocketID: socketID,
            });
        };

        pc.oniceconnectionstatechange = (e) => {
            console.log(e);
        };

        pc.ontrack = (e) => {
            console.log("ontrack success");
            setUsers((oldUsers) =>
            oldUsers
                .filter((user) => user.id !== socketID)
                .concat({
                id: socketID,
                stream: e.streams[0],
                })
            );
        };

        // return pc
        return pc;
        } catch (e) {
        console.error(e);
        return undefined;
        }
    }, []);

    const createReceivePC = useCallback(
        (id: string) => {
        try {
            console.log(`socketID(${id}) user entered`);
            const pc = createReceiverPeerConnection(id);
            if (!(socketRef.current && pc)) return;
            createReceiverOffer(pc, id);
        } catch (error) {
            console.log(error);
        }
        },
        [createReceiverOffer, createReceiverPeerConnection]
    );

    const createSenderOffer = useCallback(async () => {
        try {
        if (!sendPCRef.current) return;
        const sdp = await sendPCRef.current.createOffer({
            offerToReceiveAudio: false,
            offerToReceiveVideo: false,
        });
        console.log("create sender offer success");
        await sendPCRef.current.setLocalDescription(
            new RTCSessionDescription(sdp)
        );

        if (!socketRef.current) return;
        socketRef.current.emit("senderOffer", {
            sdp,
            senderSocketID: socketRef.current.id,
            roomID: "1234",
        });
        } catch (error) {
        console.log(error);
        }
    }, []);

    const createSenderPeerConnection = useCallback(() => {
        const pc = new RTCPeerConnection(pc_config);

        pc.onicecandidate = (e) => {
        if (!(e.candidate && socketRef.current)) return;
        console.log("sender PC onicecandidate");
        socketRef.current.emit("senderCandidate", {
            candidate: e.candidate,
            senderSocketID: socketRef.current.id,
        });
        };

        pc.oniceconnectionstatechange = (e) => {
        console.log(e);
        };

        if (localStreamRef.current) {
        console.log("add local stream");
        localStreamRef.current.getTracks().forEach((track) => {
            if (!localStreamRef.current) return;
            pc.addTrack(track, localStreamRef.current);
        });
        } else {
        console.log("no local stream");
        }

        sendPCRef.current = pc;
    }, []);

    const getLocalStream = useCallback(async () => {
        try {
        const stream = await navigator.mediaDevices.getUserMedia({
            audio: true,
            video: {
            width: 240,
            height: 240,
            },
        });
        localStreamRef.current = stream;
        if (localVideoRef.current){
            localVideoRef.current.srcObject = stream;
            localVideoRef.current.play();
        }
        if (!socketRef.current) return;

        createSenderPeerConnection();
        await createSenderOffer();

        socketRef.current.emit("joinRoom", {
            id: socketRef.current.id,
            roomID: "1234",
        });
        } catch (e) {
        console.log(`getUserMedia error: ${e}`);
        }
    }, [createSenderOffer, createSenderPeerConnection]);

    useEffect(() => {
        socketRef.current = io.connect(SOCKET_SERVER_URL);
        getLocalStream();

        socketRef.current.on("userEnter", (data: { id: string }) => {
        createReceivePC(data.id);
        });

        socketRef.current.on(
        "allUsers",
        (data: { users: Array<{ id: string }> }) => {
            data.users.forEach((user) => createReceivePC(user.id));
        }
        );

        socketRef.current.on("userExit", (data: { id: string }) => {
        closeReceivePC(data.id);
        setUsers((users) => users.filter((user) => user.id !== data.id));
        });

        socketRef.current.on(
        "getSenderAnswer",
        async (data: { sdp: RTCSessionDescription }) => {
            try {
            if (!sendPCRef.current) return;
            console.log("get sender answer");
            console.log(data.sdp);
            await sendPCRef.current.setRemoteDescription(
                new RTCSessionDescription(data.sdp)
            );
            } catch (error) {
            console.log(error);
            }
        }
        );

        socketRef.current.on(
        "getSenderCandidate",
        async (data: { candidate: RTCIceCandidateInit }) => {
            try {
            if (!(data.candidate && sendPCRef.current)) return;
            console.log("get sender candidate");
            await sendPCRef.current.addIceCandidate(
                new RTCIceCandidate(data.candidate)
            );
            console.log("candidate add success");
            } catch (error) {
            console.log(error);
            }
        }
        );

        socketRef.current.on(
        "getReceiverAnswer",
        async (data: { id: string; sdp: RTCSessionDescription }) => {
            try {
            console.log(`get socketID(${data.id})'s answer`);
            const pc: RTCPeerConnection = receivePCsRef.current[data.id];
            if (!pc) return;
            await pc.setRemoteDescription(data.sdp);
            console.log(`socketID(${data.id})'s set remote sdp success`);
            } catch (error) {
            console.log(error);
            }
        }
        );

        socketRef.current.on(
        "getReceiverCandidate",
        async (data: { id: string; candidate: RTCIceCandidateInit }) => {
            try {
            console.log(data);
            console.log(`get socketID(${data.id})'s candidate`);
            const pc: RTCPeerConnection = receivePCsRef.current[data.id];
            if (!(pc && data.candidate)) return;
            await pc.addIceCandidate(new RTCIceCandidate(data.candidate));
            console.log(`socketID(${data.id})'s candidate add success`);
            } catch (error) {
            console.log(error);
            }
        }
        );

        return () => {
        if (socketRef.current) {
            socketRef.current.disconnect();
        }
        if (sendPCRef.current) {
            sendPCRef.current.close();
        }
        users.forEach((user) => closeReceivePC(user.id));
        };
        // eslint-disable-next-line
    }, [
        closeReceivePC,
        createReceivePC,
        createSenderOffer,
        createSenderPeerConnection,
        getLocalStream,
    ]);


    return(
        <>
            <div className="practiceroom-wrap">
                <SideMenu/>
                <div className="practice-video-wrap">
                    <div className="practice-title">
                        <div className="pre-icon">
                            <Image src={LeftArrowIcon} alt=""/>
                        </div>
                        <div className="room-title">
                            <h3>랜덤플레이 댄스 방 제목</h3>
                            <span>2013년 7월 3일</span>
                        </div>
                    </div>

                    <div className="video-content">
                        <div className="my-video">
                            <video src="" playsInline ref={localVideoRef}></video>
                        </div>
                        <div className="yours-video">
                            {users.map((user, index) => (
                                <Video key={index} email={user.email} stream={user.stream} />
                            ))}
                        </div>
                        <div className="control-wrap">
                            <ul>
                                <li onMouseEnter = {reflectHover} onMouseLeave = {reflectLeave}>
                                    <button>
                                    {reflect ? <Image src={ReflectHoverIcon} alt=""/> : <Image src={ReflectIcon} alt=""/>}
                                    </button>
                                </li>
                                <li onMouseEnter = {micHover} onMouseLeave = {micLeave}>
                                    <button>
                                    {mic ? <Image src={MicHoverIcon} alt=""/> : <Image src={MicIcon} alt=""/>}
                                    </button>
                                </li>
                                <li><button className="exit-button">{lang==="en" ? "End Practice" : lang==="cn" ? "结束练习" : "연습 종료하기" }</button></li>
                                <li onMouseEnter = {cameraHover} onMouseLeave = {cameraLeave}>
                                    <button>
                                    {camera ? <Image src={CameraHoverIcon} alt=""/> : <Image src={CameraIcon} alt=""/>}
                                    </button>
                                </li>
                                <li >
                                    <button onMouseEnter = {moreDotHover} onMouseLeave = {moreDotLeave}>
                                    {moredot ? <Image src={MoreDotHoverIcon} alt=""/> : <Image src={MoreIcon} alt=""/>}
                                    </button>
                                </li>
                            </ul>
                        </div>
                    </div>
                    
                </div>
                <div className="participant-wrap">
                    <div className="participant-list">
                        <div className="participant-list-title">
                            <h3>{lang==="en" ? "List of participants" : lang==="cn" ? "参与者列表" : "참여자 목록" }</h3>
                            <span><Image src={GroupIcon} alt=""/>{users.length+1}{lang==="en" ? "people" : lang==="cn" ? "个人" : "명" }</span>
                        </div>
                        <div className="participant-list-content">
                            <ul>
                                <li className="on">
                                        <Image src={ChatDefaultImg} alt=""/>
                                        <span>임시 이름</span>
                                </li>
                                
                            </ul>
                        </div>
                    </div>
                    <div className="chat-wrap">
                        <div className="chat-title">
                            <h3>{lang==="en" ? "Chatting" : lang==="cn" ? "聊天" : "채팅하기" }</h3>
                        </div>
                        <div className="chat-content-wrap">
                            <div className="chat-read" ref={chatContent}>
                                <ul className="chat-content">
                                {
                                    msgList.map((data) => {
                                        return (
                                            <>
                                                <li>
                                                    <div className="chat-user-img">
                                                        <Image src={ChatDefaultImg} alt=""/>
                                                    </div>
                                                    <div className="chat-user-msg">
                                                        <span>김싸피</span>
                                                        <p>{data}</p>
                                                    </div>
                                                </li>
                                            </>
                                        )
                                    })
                                }
                                </ul>
                            </div>
                            <div className="chat-write">
                                <input type="text" ref={inputChat} onKeyPress={handleKeyPress} placeholder={lang==="en" ? "Send to everyone" : lang==="cn" ? "传送给所有人" : "모두에게 전송" }/>
                                <button onClick={sendMessage}><Image src={sendImg} alt=""/></button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}

export default DanceRoom;