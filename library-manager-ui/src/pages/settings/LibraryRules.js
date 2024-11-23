import { useEffect, useState } from 'react';
import { Button, message } from 'antd';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import 'react-quill/dist/quill.core.css';
import { formats, modules } from '~/common/editorConfig';
import { getLibraryRules, updateLibraryRules } from '~/services/systemSettingService';

function LibraryRules() {
    const [libraryRules, setLibraryRules] = useState('');
    const [messageApi, contextHolder] = message.useMessage();

    const handleSave = async () => {
        try {
            const content = { content: libraryRules };

            const response = await updateLibraryRules(content);
            if (response.status === 200) {
                const { message } = response.data.data;
                messageApi.success(message);
            }
        } catch (error) {
            const errorMessage = error.response?.data?.message || 'Có lỗi xảy ra khi sửa.';
            messageApi.error(errorMessage);
        }
    };

    useEffect(() => {
        const fetchLibraryRules = async () => {
            try {
                const response = await getLibraryRules();
                if (response.status === 200) {
                    setLibraryRules(response.data.data);
                }
            } catch (error) {}
        };

        fetchLibraryRules();
    }, []);

    return (
        <>
            {contextHolder}

            <h2>Thiết lập nội quy thư viện</h2>
            <form>
                <div className="row g-3">
                    <div className="col-md-9">
                        <span>
                            <span className="text-danger">*</span>Nội quy thư viện:
                        </span>
                        <ReactQuill
                            className="custom-quill"
                            placeholder="Nhập nội quy thư viện"
                            modules={modules}
                            formats={formats}
                            value={libraryRules}
                            onChange={setLibraryRules}
                        />
                    </div>
                    <div className="col-12">
                        <Button type="primary" onClick={handleSave}>
                            Lưu
                        </Button>
                    </div>
                </div>
            </form>
        </>
    );
}

export default LibraryRules;
