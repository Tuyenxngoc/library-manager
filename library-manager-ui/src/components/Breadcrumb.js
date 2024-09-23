import { Link } from 'react-router-dom';

function Breadcrumb({ items }) {
    return (
        <ul className="breadcrumb">
            {items.map((item, index) => (
                <li key={index}>{item.url ? <Link to={item.url}>{item.label}</Link> : <span>{item.label}</span>}</li>
            ))}
        </ul>
    );
}

export default Breadcrumb;
