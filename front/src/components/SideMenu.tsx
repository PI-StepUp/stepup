import CloseIcon from "/public/images/close-circle-icon.svg"
import CamIcon from "/public/images/cam-icon.svg"
import ChatIcon from "/public/images/icon-chat.svg"

import Image from "next/image"

const SideMenu = () => {
    return(
        <>
            <div className="sidemenu-wrap">
                <ul>
                    <li>
                        <Image src={ChatIcon} alt=""/>
                        <span>채팅 참여하기</span>
                    </li>
                    <li>
                        <Image src={CamIcon} alt="" className="big-icon"/>
                    </li>
                    <li>
                        <Image src={CloseIcon} alt=""/>
                        <span>연습 종료하기</span>
                    </li>
                </ul>
            </div>
        </>
    )
}

export default SideMenu;