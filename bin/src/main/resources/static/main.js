<script>
    function showContent(sectionId) {
        // Ẩn tất cả các phần nội dung
        document.querySelectorAll('.content-section').forEach(section => {
            section.style.display = 'none';
        });

        // Hiển thị phần được chọn
        document.getElementById(sectionId).style.display = 'block';
    }
	
	document.querySelectorAll('.admin-menu-item').forEach(item => {
        item.addEventListener('click', function() {
            document.querySelectorAll('.admin-menu-item').forEach(i => i.classList.remove('active'));
            this.classList.add('active');
        });
    });
</script>