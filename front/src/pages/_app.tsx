import "../styles/global.scss"

import type {AppProps} from 'next/app';

const StepUp = ({Component} : AppProps) => {
    return (
        <>
            <Component/>
        </>
    )
}

export default StepUp;