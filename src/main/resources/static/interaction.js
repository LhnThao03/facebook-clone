<script th:inline="javascript">
    document.addEventListener('DOMContentLoaded', function() {
    const shareDialog = document.getElementById("shareDialog");
    const closeModalBtn = document.querySelector(".close-modal");
    const cancelShareBtn = document.querySelector(".cancel-share");

    function closeShareDialog() {
        shareDialog.style.display = "none";
    }

    closeModalBtn.addEventListener("click", closeShareDialog);
    cancelShareBtn.addEventListener("click", closeShareDialog);
    // Thêm hàm showNotification
    function showNotification(message, type = 'success') {
        // Xóa notification cũ nếu có
        const existingNotification = document.querySelector('.notification');
        if (existingNotification) {
            existingNotification.remove();
        }

        const notification = document.createElement('div');
        notification.className = `notification ${type}`;
        notification.textContent = message;
        
        // Style cho notification
        Object.assign(notification.style, {
            position: 'fixed',
            bottom: '20px',
            right: '20px',
            padding: '12px 24px',
            borderRadius: '8px',
            color: 'white',
            zIndex: '1001',
            animation: 'slideInRight 0.3s ease-in-out',
            backgroundColor: type === 'success' ? '#4BB543' : '#ff3333'
        });

        document.body.appendChild(notification);

        // Tự động xóa notification sau 3 giây
        setTimeout(() => {
            notification.style.animation = 'slideOutRight 0.3s ease-in-out';
            setTimeout(() => notification.remove(), 300);
        }, 3000);
    }

    let currentPostIdForShare = null;

    // Xử lý nút Like
    document.querySelectorAll('.like-btn').forEach(button => {
        button.addEventListener('click', function() {
            const postId = this.getAttribute('data-post-id');
            const isLiked = this.getAttribute('data-liked') === 'true';
            const likeIcon = this.querySelector('.like-icon');
            const likeText = this.querySelector('.like-text');

            // Thêm class animation
            likeIcon.classList.add('animating');
            
            fetch(`/posts/${postId}/like`, {
                method: 'POST',
                credentials: 'include'
            })
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.json();
            })
            .then(data => {
                // Cập nhật số lượt like
                const likeCountElement = document.querySelector(`.like-count[data-post-id="${postId}"]`);
                if (likeCountElement) {
                    likeCountElement.textContent = `👍 ${data.likeCount}`;
                }

                // Toggle trạng thái like
                if (data.isLiked) {
                    this.classList.add('liked');
                    this.setAttribute('data-liked', 'true');
                    likeIcon.classList.remove('far');
                    likeIcon.classList.add('fas');
                } else {
                    this.classList.remove('liked');
                    this.setAttribute('data-liked', 'false');
                    likeIcon.classList.remove('fas');
                    likeIcon.classList.add('far');
                }

                // Xóa class animation sau khi hoàn thành
                setTimeout(() => {
                    likeIcon.classList.remove('animating');
                }, 400);
            })
            .catch(error => {
                console.error('Error:', error);
                showNotification('Có lỗi xảy ra khi thích bài viết', 'error');
            });
        });
    });

    // Xử lý nút Comment
    document.querySelectorAll('.comment-btn').forEach(button => {
        button.addEventListener('click', function() {
            const postId = this.getAttribute('data-post-id');
            const commentSection = document.querySelector(`.comment-section[data-post-id="${postId}"]`);
            if (commentSection) {
                commentSection.style.display = commentSection.style.display === 'none' ? 'block' : 'none';
            }
        });
    });

    // Xử lý nút xem thêm và ẩn bớt comment
    document.querySelectorAll('.view-more-btn, .hide-comments-btn').forEach(button => {
        button.addEventListener('click', function() {
            const postId = this.getAttribute('data-post-id');
            const commentsSection = this.closest('.comments-list');
            const hiddenComments = commentsSection.querySelector('.hidden-comments');
            const viewMoreBtn = commentsSection.querySelector('.view-more-btn');
            const hideCommentsBtn = commentsSection.querySelector('.hide-comments-btn');

            if (this.classList.contains('view-more-btn')) {
                // Hiển thị comments ẩn
                hiddenComments.style.display = 'block';
                // Ẩn nút xem thêm
                viewMoreBtn.style.display = 'none';
                // Hiện nút ẩn bớt
                hideCommentsBtn.style.display = 'block';
            } else {
                // Ẩn comments
                hiddenComments.style.display = 'none';
                // Hiện nút xem thêm
                viewMoreBtn.style.display = 'block';
                // Ẩn nút ẩn bớt
                hideCommentsBtn.style.display = 'none';
            }
        });
    });

    // Xử lý submit comment
    document.querySelectorAll('.submit-comment').forEach(button => {
        button.addEventListener('click', function() {
            const postId = this.getAttribute('data-post-id');
            const input = document.querySelector(`.comment-input[data-post-id="${postId}"]`);
            if (!input || !input.value.trim()) return;

            const formData = new URLSearchParams();
            formData.append('content', input.value.trim());

            fetch(`/posts/${postId}/comment`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: formData,
                credentials: 'include'
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                // Cập nhật số lượng comment
                const commentCountElement = document.querySelector(`.comment-count[data-post-id="${postId}"]`);
                if (commentCountElement) {
                    commentCountElement.textContent = `💬 ${data.totalComments}`;
                }

                // Thêm comment mới vào danh sách
                const commentsList = document.querySelector(`.comments-list[data-post-id="${postId}"]`);
                if (commentsList) {
                    const initialComments = commentsList.querySelector('.initial-comments');
                    const hiddenComments = commentsList.querySelector('.hidden-comments');
                    
                    const newCommentHtml = `
                        <div class="comment-item" style="display: flex; gap: 10px; margin-bottom: 10px; padding: 8px;">
                            <img src="${data.user.profilePicture}" class="mini-avatar">
                            <div class="comment-content" style="flex: 1;">
                                <div class="comment-bubble" style="background: #f0f2f5; padding: 8px 12px; border-radius: 18px;">
                                    <strong>${data.user.firstname} ${data.user.lastname}</strong>
                                    <p style="margin: 0;">${data.content}</p>
                                </div>
                                <div class="comment-meta" style="font-size: 12px; color: #65676B; margin-top: 4px;">
                                    <span>Vừa xong</span>
                                </div>
                            </div>
                        </div>
                    `;

                    // Nếu có ít hơn 3 comment, thêm vào initial-comments
                    if (initialComments.children.length < 3) {
                        initialComments.insertAdjacentHTML('afterbegin', newCommentHtml);
                    } else {
                        // Nếu đã có 3 comment trở lên, thêm vào hidden-comments
                        hiddenComments.insertAdjacentHTML('afterbegin', newCommentHtml);
                        
                        // Hiển thị nút "Xem thêm" nếu chưa có
                        const viewMoreBtn = commentsList.querySelector('.view-more-btn');
                        if (viewMoreBtn) {
                            viewMoreBtn.style.display = 'block';
                        }
                    }
                }

                // Xóa nội dung input
                input.value = '';
                
                // Hiển thị thông báo thành công
                showNotification('Đã thêm bình luận thành công!');
            })
            .catch(error => {
                console.error('Error:', error);
                showNotification('Có lỗi xảy ra khi thêm bình luận', 'error');
            });
        });
    });

    // Xử lý nút chia sẻ
    document.querySelectorAll('.share-btn').forEach(button => {
        button.addEventListener('click', function() {
            currentPostIdForShare = this.getAttribute('data-post-id');
            document.getElementById('shareDialog').style.display = 'flex';
        });
    });

    // Xử lý nút xác nhận chia sẻ
    document.querySelector('.confirm-share').addEventListener('click', function() {
        if (!currentPostIdForShare) return;

        fetch(`/posts/${currentPostIdForShare}/share`, {
            method: 'POST',
            credentials: 'include'
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // Cập nhật số lượt chia sẻ
            const shareCountElement = document.querySelector(`.share-count[data-post-id="${currentPostIdForShare}"]`);
            if (shareCountElement) {
                shareCountElement.textContent = `🔁 ${data.shareCount}`;
            }

            // Hiển thị thông báo thành công
            showNotification('Đã chia sẻ bài viết thành công!');
            
            // Đóng modal
            document.getElementById('shareDialog').style.display = 'none';
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('Có lỗi xảy ra khi chia sẻ bài viết!', 'error');
        });
    });

    // Thêm CSS cho animation
    const style = document.createElement('style');
    style.textContent = `
        @keyframes slideInRight {
            from {
                transform: translateX(100%);
                opacity: 0;
            }
            to {
                transform: translateX(0);
                opacity: 1;
            }
        }

        @keyframes slideOutRight {
            from {
                transform: translateX(0);
                opacity: 1;
            }
            to {
                transform: translateX(100%);
                opacity: 0;
            }
        }
    `;
    document.head.appendChild(style);

    // Xử lý nút xóa bình luận
    document.querySelectorAll('.delete-comment-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            if (!confirm('Bạn có chắc muốn xóa bình luận này?')) return;
            const commentId = this.getAttribute('data-comment-id');
            fetch(`/posts/comment/${commentId}`, {
                method: 'DELETE',
                credentials: 'include'
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // Xóa comment khỏi DOM
                    this.closest('.comment-item').remove();
                    showNotification('Đã xóa bình luận!');
                } else {
                    showNotification(data.error || 'Không thể xóa bình luận!', 'error');
                }
            })
            .catch(() => showNotification('Có lỗi xảy ra khi xóa bình luận!', 'error'));
        });
    });
	
	document.querySelectorAll('.add-friend-btn').forEach(button => {
	    button.addEventListener('click', () => {
	        const receiverId = button.getAttribute('data-user-id');

	        fetch(`friends/send-friend-request/${receiverId}`, {
	            method: 'POST',
	        })
	        .then(response => {
	            if (response.ok) {
	                button.style.display = 'none';
	                button.nextElementSibling.style.display = 'inline'; // hiện "Đã gửi lời mời"
	            } else {
	                alert("Gửi lời mời thất bại!");
	            }
	        })
	        .catch(error => {
	            console.error("Error:", error);
	        });
	    });
	});
});
</script>