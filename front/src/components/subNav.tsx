import Link from "next/link"

import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";

const subNav = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
    return (
        <>
            <div className="sub-nav-wrap">
                <ul>
                    <li><h2><Link href="/notice/list">{lang==="en" ? "Notice" : lang==="cn" ? "公告" : "공지사항" }</Link></h2></li>
                    <li><h2><Link href="/article/list">{lang==="en" ? "Communication" : lang==="cn" ? "疏通" : "소통" }</Link></h2></li>
                    <li><h2><Link href="/meeting/list">{lang==="en" ? "Offline meeting" : lang==="cn" ? "线下聚会" : "오프라인 정모" }</Link></h2></li>
                </ul>
            </div>
        </>
    )
}

export default subNav;