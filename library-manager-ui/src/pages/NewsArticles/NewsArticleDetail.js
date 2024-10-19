import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { FaRegCalendarAlt } from 'react-icons/fa';
import { Parallax } from 'react-parallax';
import { Button, Skeleton } from 'antd';
import { backgrounds } from '~/assets';
import Breadcrumb from '~/components/Breadcrumb';
import { getNewsArticleByTitleSlugForUser } from '~/services/newsArticlesService';
import classNames from 'classnames/bind';
import styles from '~/styles/NewsArticleDetail.module.scss';
import SocialIcons from '~/components/SocialIcons';

const cx = classNames.bind(styles);

function NewsArticleDetail() {
    const { id } = useParams();

    const [entityData, setEntityData] = useState(null);

    const [isLoading, setIsLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState(null);

    useEffect(() => {
        const fetchEntities = async () => {
            setIsLoading(true);
            setErrorMessage(null);
            try {
                const response = await getNewsArticleByTitleSlugForUser(id);
                const { data } = response.data;
                setEntityData(data);
            } catch (error) {
                setErrorMessage(error.response.data.message || error.message);
            } finally {
                setIsLoading(false);
            }
        };

        fetchEntities();
    }, [id]);

    const items = [
        {
            label: 'Trang chủ',
            url: '/',
        },
        {
            label: 'Chi tiết bài viết',
        },
    ];

    return (
        <>
            <Parallax bgImage={backgrounds.bgparallax7} strength={500}>
                <div className="innerbanner">
                    <div className="container">
                        <div className="row justify-content-center">
                            <div className="col-auto">
                                <h1>Chi tiết bài viết</h1>
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
                        {isLoading ? (
                            <>
                                <Skeleton active paragraph={{ rows: 5 }} />
                            </>
                        ) : errorMessage ? (
                            <div className="alert alert-danger p-2" role="alert">
                                Lỗi: {errorMessage}
                            </div>
                        ) : (
                            <>
                                <figure className={cx('newsdetailimg')}>
                                    <img src={entityData.imageUrl} alt="description" />

                                    <figcaption className={cx('author')}>
                                        <span className="bookwriter">Tác giả: Admin</span>
                                        <ul className="postmetadata">
                                            <li>
                                                <FaRegCalendarAlt />
                                                <i className="ms-2">{entityData.createdDate}</i>
                                            </li>
                                        </ul>
                                    </figcaption>
                                </figure>

                                <div className={cx('newsdetail')}>
                                    <div className={cx('posttitle')}>
                                        <h3>{entityData.title}</h3>
                                    </div>

                                    <div className={cx('description')}>
                                        <q>{entityData.description}</q>
                                        <div
                                            className="ql-snow ql-editor p-0 mt-4"
                                            style={{ whiteSpace: 'normal', overflowWrap: 'anywhere' }}
                                            dangerouslySetInnerHTML={{ __html: entityData.content }}
                                        />
                                    </div>
                                    <div className={cx('tagsshare')}>
                                        <span>Share:</span>
                                        <SocialIcons />
                                    </div>
                                </div>
                            </>
                        )}
                    </div>
                </div>
            </div>
        </>
    );
}

export default NewsArticleDetail;
