import { Button } from 'antd';
import { useState } from 'react';
import { Parallax } from 'react-parallax';
import { backgrounds } from '~/assets';
import Breadcrumb from '~/components/Breadcrumb';
import Post from '~/components/Post';
import SectionHeader from '~/components/SectionHeader';

function News() {
    const [posts, setPosts] = useState([1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]);

    const items = [
        {
            label: 'Trang chủ',
            url: '/',
        },
        {
            label: 'Danh sách tin tức',
        },
    ];

    return (
        <>
            <Parallax bgImage={backgrounds.bgparallax7} strength={500}>
                <div className="innerbanner">
                    <div className="container">
                        <div className="row justify-content-center">
                            <div className="col-auto">
                                <h1>Danh sách tin tức</h1>
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
                <div className="row mb-4">
                    <div className="col-3">
                        <Button block>Phân loại tin tức</Button>
                    </div>
                    <div className="col-9">
                        <SectionHeader title="Danh sách tin tức" subtitle="Tin tức và bài viết mới nhất" />
                        <div className="row">
                            {posts.map((data, index) => (
                                <div className="col-12">
                                    <Post className="mx-2 my-1" key={index} data={data} layout="horizontal" />
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default News;
