import { Parallax } from 'react-parallax';
import { backgrounds } from '~/assets';
import Breadcrumb from '~/components/Breadcrumb';
import SectionHeader from '~/components/SectionHeader';

import { Collapse, Tabs } from 'antd';
import BasicSearchForm from '~/components/BasicSearchForm';
import AdvancedSearchForm from '~/components/AdvancedSearchForm';

function Search() {
    const breadcrumbItems = [
        {
            label: 'Trang chủ',
            url: '/',
        },
        {
            label: 'Tìm kiếm nâng cao',
        },
    ];

    const searchTabs = [
        {
            key: '1',
            label: 'Cơ bản',
            children: <BasicSearchForm />,
        },
        {
            key: '2',
            label: 'Nâng cao',
            children: <AdvancedSearchForm />,
        },
    ];

    const collapseItems = [
        {
            key: '1',
            label: 'Tổng số ấn phẩm',
            children: 10659,
        },
        {
            key: '2',
            label: 'Huớng dẫn tra cứu',
            children: (
                <ul>
                    <li>
                        <strong>Tìm cơ bản:</strong>
                        Tìm theo Loại tài liệu, Từ khóa, Tên tài liệu, Tác giả, Năm xuất bản
                    </li>
                    <li>
                        <strong>Tìm nâng cao:</strong>
                        Tìm theo theo toán tử AND, OR, NOT
                    </li>
                </ul>
            ),
        },
    ];

    return (
        <>
            <Parallax bgImage={backgrounds.bgparallax7} strength={500}>
                <div className="innerbanner">
                    <div className="container">
                        <div className="row justify-content-center">
                            <div className="col-auto">
                                <h1>Tìm kiếm nâng cao</h1>
                            </div>
                        </div>

                        <div className="row justify-content-center">
                            <div className="col-auto">
                                <Breadcrumb items={breadcrumbItems} />
                            </div>
                        </div>
                    </div>
                </div>
            </Parallax>

            <div className="container sectionspace">
                <div className="row mb-4">
                    <div className="col-3">
                        <Collapse items={collapseItems} defaultActiveKey={['1']} />
                    </div>
                    <div className="col-9">
                        <SectionHeader title="Tìm kiếm nâng cao" subtitle="Tìm kiếm" />
                        <Tabs className="mt-4" defaultActiveKey="1" size="large" items={searchTabs} />
                    </div>
                </div>
                <div className="row">
                    <SectionHeader title="Thư viện sách" subtitle="Tìm kiếm nâng cao" />
                </div>
            </div>
        </>
    );
}

export default Search;
