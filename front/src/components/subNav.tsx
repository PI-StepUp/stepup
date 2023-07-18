import Link from "next/link"

const subNav = () => {
    return (
        <>
            <div className="sub-nav-wrap">
                <ul>
                    <li><h2><Link href="">공지사항</Link></h2></li>
                    <li><h2><Link href="">소통</Link></h2></li>
                    <li><h2><Link href="">오프라인 정모</Link></h2></li>
                </ul>
            </div>
        </>
    )
}

export default subNav;