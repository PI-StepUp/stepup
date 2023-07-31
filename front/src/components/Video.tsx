import React, { useEffect, useRef, useState } from 'react';

interface Props {
	email: string;
	stream: MediaStream;
	muted?: boolean;
}

const Video = ({ email, stream, muted }: Props) => {
	const ref = useRef<HTMLVideoElement>(null);
	const [isMuted, setIsMuted] = useState<boolean>(false);

	useEffect(() => {
		if (ref.current) ref.current.srcObject = stream;
		if (muted) setIsMuted(muted);
	}, [stream, muted]);

	return (
		<div>
			<video ref={ref} muted={isMuted} autoPlay />
		</div>
	);
};

export default Video;