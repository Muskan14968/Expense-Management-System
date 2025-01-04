import user from '../../assets/images/user.png';
import useProfileImage from '../../hooks/useProfileImage';
import Loading from "../utils/loading";

function ProfileCard({ username, email }) {

    const [profileImg, isLoading, uploadProfileImage, removeProfileImage] = useProfileImage();

    const handleFileChange = async (e) => {
        const selectedFile = e.target.files[0];
        const maxSize = 10 * 1024 * 1024;
        if (selectedFile && selectedFile.size > maxSize) {
            return;
        }
        await uploadProfileImage(selectedFile);
    }

    return (
        <div 
            className='profile' 
            style={{ 
                maxWidth: '400px', 
                flexDirection: 'column', 
                justifyContent: 'center', 
                padding: '25px' 
            }}
        >
            <style>
                {`
                    .action label {
                        cursor: pointer;
                        color: #007bff;
                        text-decoration: underline;
                        font-size: 14px;
                        display: inline-block;
                        margin-top: 10px;
                    }

                    .action-link {
                        cursor: pointer;
                        color: #007bff;
                        text-decoration: underline;
                        transition: color 0.2s;
                    }

                    .action-link:hover {
                        color: #0056b3;
                    }

                    .action input[type="file"] {
                        display: none;
                    }
                `}
            </style>

            {
                (isLoading) ? 
                    <Loading /> : 
                    (
                        <>
                            <div className="profile-img">
                                {!profileImg && <img src={user} width={140} height={140} alt='user' />}
                                {profileImg && <img src={profileImg} width={140} height={140} alt='user' />}
                                <div className="action">
                                    {profileImg && 
                                        <label 
                                            onClick={removeProfileImage} 
                                            className="action-link"
                                        >
                                            Remove image
                                        </label>
                                    }
                                    <label className="action-link">
                                        Change image
                                        <input type="file" onChange={handleFileChange} accept=".jpg, .jpeg, .png" />
                                    </label>
                                </div>
                            </div>

                            <div style={{ alignItems: 'center' }}>
                                <h3 style={{ color: 'var(--text)' }}>{username}</h3>
                                <p>{email}</p>
                            </div>
                        </>
                    )
            }
        </div>
    )
}

export default ProfileCard;
