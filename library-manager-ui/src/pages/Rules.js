import { Parallax } from 'react-parallax';
import { backgrounds } from '~/assets';
import Breadcrumb from '~/components/Breadcrumb';
import SectionHeader from '~/components/SectionHeader';

import classNames from 'classnames/bind';
import styles from '~/styles/Rules.module.scss';

const cx = classNames.bind(styles);

function Rules() {
    const items = [
        {
            label: 'Trang chủ',
            url: '/',
        },
        {
            label: 'Nội quy chung của thư viện',
        },
    ];

    return (
        <>
            <Parallax bgImage={backgrounds.bgparallax7} strength={500}>
                <div className="container py-5">
                    <div className="row justify-content-center">
                        <div className="col-auto py-5 text-center">
                            <h1>Về chúng tôi</h1>
                            <Breadcrumb items={items} />
                        </div>
                    </div>
                </div>
            </Parallax>

            <div className="container sectionspace">
                <div className="row">
                    <div className="col-12 mb-4">
                        <SectionHeader title="Nội quy của thư viện" subtitle="Nội quy" />
                    </div>
                    <div className="col-12">
                        <div className={cx('rules')}>
                            <p>
                                <strong>
                                    Bạn đọc đến đọc sách, ngoài việc chấp hành các quy định chung của thư viện cần thực
                                    hiện các quy định sau đây:
                                </strong>
                            </p>
                            <ul>
                                <li>
                                    Không mang túi xách, cặp, sách, báo – tạp chí in vào phòng đọc (chấp nhận sách, báo
                                    dạng photocopy)
                                </li>
                                <li>Xuất trình thẻ đọc, chứng minh thư, giấy giới thiệu tại bộ phận thủ thư</li>
                                <li>Chỉ đọc tại chỗ, không mang tài liệu ra khỏi phòng đọc, ra ngoài thư viện</li>
                                <li>
                                    Không cắt xén, xé trang tài liệu, khi phát hiện sách thiếu trang, yêu cầu báo ngay
                                    cho thủ thư, nếu không bạn đọc hoàn toàn chịu trách nhiệm
                                </li>
                                <li>
                                    Bạn đọc nào vi phạm nội quy, tùy từng mức độ sẽ có hình thức xử lý thích hợp: thu
                                    thẻ đọc, bồi thường, thông báo về cơ quan, trường học…hoặc truy tố trước pháp luật
                                </li>
                                <li>Không hút thuốc, chất dễ cháy nổ vào phòng đọc, giữ gìn vệ sinh chung</li>
                                <li>
                                    Không nói chuyện riêng, không nghe điện thoại tại phòng đọc, yêu cầu điện thoại để
                                    chế độ rung
                                </li>
                                <li>
                                    Khi có nhu cầu photocopy tài liệu, cần liên hệ với thủ thư để được chỉ dẫn cụ thể.
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default Rules;
