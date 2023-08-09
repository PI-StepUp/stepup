import {useRef} from "react";
import Link from "next/link"

// import { useRecoilState } from "recoil";
// import { LanguageState } from "states/states";

const subNav = ({linkNo} : any) => {
    // const [lang, setLang] = useRecoilState(LanguageState);
    return (
        <>
            <div className="sub-nav-wrap">
                <ul>
                    {linkNo === "1" ? <li className="active"><h2><Link href="/notice/list">공지사항</Link></h2></li> : <li><h2><Link href="/notice/list">공지사항</Link></h2></li>}
                    {linkNo === "2" ? <li className="active"><h2><Link href="/article/list">소통</Link></h2></li> : <li><h2><Link href="/article/list">소통</Link></h2></li>}
                    {linkNo === "3" ? <li className="active"><h2><Link href="/meeting/list">오프라인 정모</Link></h2></li> : <li><h2><Link href="/meeting/list">오프라인 정모</Link></h2></li>}
                </ul>
            </div>
        </>
    )
}

export default subNav;