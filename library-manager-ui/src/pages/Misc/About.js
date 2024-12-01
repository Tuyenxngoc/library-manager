import { useEffect, useState } from 'react';
import { Parallax } from 'react-parallax';
import { backgrounds } from '~/assets';
import Breadcrumb from '~/components/Breadcrumb';
import SectionHeader from '~/components/SectionHeader';
import { getLibraryInfo } from '~/services/systemSettingService';

function About() {
    const [libraryInfo, setLibraryInfo] = useState({});

    const items = [
        {
            label: 'Trang chủ',
            url: '/',
        },
        {
            label: 'Thông tin về chúng tôi',
        },
    ];

    useEffect(() => {
        const fetchEntities = async () => {
            try {
                const response = await getLibraryInfo();
                const { data } = response.data;
                if (data) {
                    setLibraryInfo(data);
                }
            } catch (error) {}
        };

        fetchEntities();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    return (
        <>
            <Parallax bgImage={backgrounds.bgparallax7} strength={500}>
                <div className="innerbanner">
                    <div className="container">
                        <div className="row justify-content-center">
                            <div className="col-auto">
                                <h1>Về chúng tôi</h1>
                            </div>
                        </div>

                        <div className="row justify-content-center">
                            <div className="col-auto">
                                <Breadcrumb items={items} />
                            </div>
                        </div>
                    </div>
                </div>
            </Parallax>

            <div className="container sectionspace">
                <div className="row">
                    <div className="col-12 mb-4">
                        <SectionHeader title={<h2 className="mb-0">Về chúng tôi</h2>} subtitle="Xin chào!" />
                    </div>
                    <div className="col-12">
                        <div
                            className="ql-snow ql-editor p-0 mt-4"
                            style={{ whiteSpace: 'normal', overflowWrap: 'anywhere' }}
                            dangerouslySetInnerHTML={{ __html: libraryInfo.introduction }}
                        />
                    </div>
                </div>
            </div>
        </>
    );
}

export default About;
