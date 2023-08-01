import Link from "next/link"

const subNav = () => {
    return (
        <>
            <div className="sub-nav-wrap">
                <ul>
                    <li><h2><Link href="/notice/list">공지사항</Link></h2></li>
                    <li><h2><Link href="/article/list">소통</Link></h2></li>
                    <li><h2><Link href="/meeting/list">오프라인 정모</Link></h2></li>
                </ul>
            </div>
        </>
    )
}

export default subNav;