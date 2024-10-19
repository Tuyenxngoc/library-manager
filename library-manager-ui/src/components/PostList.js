import SectionHeader from './SectionHeader';

import classNames from 'classnames/bind';
import Slider from 'react-slick';
import styles from '~/styles/PostList.module.scss';
import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Post from './Post';
import { getNewsArticlesForUser } from '~/services/newsArticlesService';
import { INITIAL_FILTERS, INITIAL_META } from '~/common/commonConstants';
import queryString from 'query-string';

const cx = classNames.bind(styles);
function PostList() {
    const sliderRef = useRef(null);
    const navigate = useNavigate();

    const [meta, setMeta] = useState(INITIAL_META);
    const [filters, setFilters] = useState(INITIAL_FILTERS);

    const [entityData, setEntityData] = useState(null);

    const [isLoading, setIsLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState(null);

    useEffect(() => {
        const fetchEntities = async () => {
            setIsLoading(true);
            setErrorMessage(null);
            try {
                const params = queryString.stringify(filters);
                const response = await getNewsArticlesForUser(params);
                const { meta, items } = response.data.data;
                setEntityData(items);
                setMeta(meta);
            } catch (error) {
                setErrorMessage(error.message);
            } finally {
                setIsLoading(false);
            }
        };

        fetchEntities();
    }, [filters]);

    const settings = {
        dots: false,
        infinite: true,
        speed: 500,
        slidesToShow: 5,
        slidesToScroll: 1,
        swipeToSlide: true,
    };

    const goToNextSlide = () => {
        sliderRef.current.slickNext();
    };

    const goToPrevSlide = () => {
        sliderRef.current.slickPrev();
    };

    const handleViewAll = () => {
        navigate('/all-products');
    };

    return (
        <section className={cx('wrapper', 'sectionspace')}>
            <div className="container">
                <div className="row mb-3">
                    <div className="col-12">
                        <SectionHeader
                            subtitle="Tin tức & bài viết mới nhất"
                            title="Tin nổi bật trong ngày"
                            onPrev={goToPrevSlide}
                            onNext={goToNextSlide}
                            onViewAll={handleViewAll}
                        />
                    </div>
                </div>
                <div className="row">
                    <div className="col-12">
                        {isLoading ? (
                            <>Loading</>
                        ) : (
                            <Slider ref={sliderRef} {...settings}>
                                {entityData.map((data, index) => (
                                    <Post className="mx-2 my-1" key={index} data={data} />
                                ))}
                            </Slider>
                        )}
                    </div>
                </div>
            </div>
        </section>
    );
}

export default PostList;
